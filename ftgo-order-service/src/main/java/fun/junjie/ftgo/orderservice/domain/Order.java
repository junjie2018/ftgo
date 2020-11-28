package fun.junjie.ftgo.orderservice.domain;

import fun.junjie.ftgo.common.Money;
import fun.junjie.ftgo.common.UnsupportedStateTransitionException;
import fun.junjie.ftgo.orderservice.api.OrderDetails;
import fun.junjie.ftgo.orderservice.api.OrderLineItem;
import fun.junjie.ftgo.orderservice.api.events.*;
import fun.junjie.ftgo.orderservice.domain.exceptions.OrderMinimumNotMetException;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static fun.junjie.ftgo.orderservice.api.events.OrderState.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


@Entity
@Table(name = "orders")
@Access(AccessType.FIELD)
@NoArgsConstructor
public class Order {

    public static ResultWithDomainEvents<Order, OrderDomainEvent> createOrder(
            long consumerId,
            Restaurant restaurant,
            DeliveryInformation deliveryInformation,
            List<OrderLineItem> orderLineItems) {

        Order order = new Order(consumerId, restaurant.getId(), deliveryInformation, orderLineItems);

        List<OrderDomainEvent> events = singletonList(new OrderCreatedEvent(
                new OrderDetails(
                        consumerId,
                        restaurant.getId(),
                        orderLineItems,
                        order.getOrderTotal()),
                deliveryInformation.getDeliveryAddress(),
                restaurant.getName()));

        return new ResultWithDomainEvents<>(order, events);
    }

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Version
    @Getter
    private Long version;

    @Enumerated(EnumType.STRING)
    @Getter
    private OrderState state;

    @Getter
    private Long consumerId;

    @Getter
    private Long restaurantId;

    @Embedded
    private OrderLineItems orderLineItems;

    @Embedded
    @Getter
    private DeliveryInformation deliveryInformation;

    @Embedded
    private PaymentInformation paymentInformation;

    @Embedded
    private Money orderMinimum = new Money(Integer.MAX_VALUE);

    public Order(long consumerId,
                 long restaurantId,
                 DeliveryInformation deliveryInformation,
                 List<OrderLineItem> orderLineItems) {
        this.consumerId = consumerId;
        this.restaurantId = restaurantId;
        this.deliveryInformation = deliveryInformation;
        this.orderLineItems = new OrderLineItems(orderLineItems);
        this.state = APPROVAL_PENDING;
    }

    public Money getOrderTotal() {
        return orderLineItems.orderTotal();
    }

    public List<OrderLineItem> getLineItems() {
        return orderLineItems.getLineItems();
    }

    public List<OrderDomainEvent> cancel() {
        switch (state) {
            case APPROVED:
                this.state = OrderState.CANCEL_PENDING;
                return emptyList();
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public List<OrderDomainEvent> undoPendingCancel() {
        switch (state) {
            case CANCEL_PENDING:
                this.state = OrderState.APPROVED;
                return emptyList();
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public List<OrderDomainEvent> noteCancelled() {
        switch (state) {
            case CANCEL_PENDING:
                this.state = OrderState.CANCELED;
                return singletonList(new OrderCancelled());
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public List<OrderDomainEvent> noteApproved() {
        switch (state) {
            case APPROVAL_PENDING:
                this.state = OrderState.APPROVED;
                return singletonList(new OrderAuthorized());
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public List<OrderDomainEvent> noteRejected() {
        switch (state) {
            case APPROVAL_PENDING:
                this.state = OrderState.REJECTED;
                return singletonList(new OrderRejected());
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public List<OrderDomainEvent> noteReversionAuthorization() {
        return null;
    }

    public List<OrderDomainEvent> rejectRevision() {
        switch (state) {
            case REVISION_PENDING:
                this.state = OrderState.APPROVED;
                return emptyList();
            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public List<OrderDomainEvent> confirmRevision(OrderRevision orderRevision) {
        switch (state) {
            case REVISION_PENDING:
                LineItemQuantityChange licd = orderLineItems.lineItemQuantityChange(orderRevision);

                orderRevision.getDeliveryInformation().ifPresent(newDi -> this.deliveryInformation = newDi);

                if (orderRevision.getRevisedOrderLineItems() != null
                        && orderRevision.getRevisedOrderLineItems().size() > 0) {
                    orderLineItems.updateLineItems(orderRevision);
                }

                this.state = APPROVED;

                return singletonList(new OrderRevised(orderRevision,
                        licd.currentOrderTotal,
                        licd.newOrderTotal));

            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }

    public ResultWithDomainEvents<LineItemQuantityChange, OrderDomainEvent> revise(OrderRevision orderRevision) {
        switch (state) {
            case APPROVED:
                LineItemQuantityChange change = orderLineItems.lineItemQuantityChange(orderRevision);

                if (change.newOrderTotal.isGreaterThanOrEqual(orderMinimum)) {
                    throw new OrderMinimumNotMetException();
                }

                this.state = REVISION_PENDING;

                return new ResultWithDomainEvents<>(
                        change,
                        singletonList(new OrderRevisionProposed(
                                orderRevision,
                                change.currentOrderTotal,
                                change.newOrderTotal)));

            default:
                throw new UnsupportedStateTransitionException(state);
        }
    }
}
