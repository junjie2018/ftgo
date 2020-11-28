package fun.junjie.ftgo.orderservice.domain;

import fun.junjie.ftgo.orderservice.api.OrderDetails;
import fun.junjie.ftgo.orderservice.api.events.OrderDomainEvent;
import fun.junjie.ftgo.orderservice.api.OrderLineItem;
import fun.junjie.ftgo.orderservice.domain.exceptions.InvalidMenuItemIdException;
import fun.junjie.ftgo.orderservice.domain.exceptions.OrderNotFoundException;
import fun.junjie.ftgo.orderservice.domain.exceptions.RestaurantNotFoundException;
import fun.junjie.ftgo.orderservice.domain.repositories.OrderRepository;
import fun.junjie.ftgo.orderservice.domain.repositories.RestaurantRespository;
import fun.junjie.ftgo.orderservice.sagas.createorder.CreateOrderSaga;
import fun.junjie.ftgo.orderservice.sagas.createorder.CreateOrderSagaState;
import fun.junjie.ftgo.orderservice.web.MenuItemIdAndQuantity;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Slf4j
public class OrderService {

    private RestaurantRespository restaurantRespository;
    private OrderRepository orderRepository;

    private OrderDomainEventPublisher orderAggregateEventPublisher;

    private SagaInstanceFactory sagaInstanceFactory;

    private CreateOrderSaga createOrderSaga;

    @Transactional
    public Order createOrder(
            long consumerId,
            long restaurantId,
            DeliveryInformation deliveryInformation,
            List<MenuItemIdAndQuantity> lineItems) {

        // region 暂时不需要理解

        Restaurant restaurant = restaurantRespository
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        List<OrderLineItem> orderLineItems = makeOrderLineItems(lineItems, restaurant);

        // endregion

        // 已经理解了
        ResultWithDomainEvents<Order, OrderDomainEvent> orderAndEvents =
                Order.createOrder(consumerId, restaurant, deliveryInformation, orderLineItems);

        // 已经理解了
        Order order = orderAndEvents.result;
        orderRepository.save(order);

        /*
            1.需要知道orderAggregateEventPublisher如何发布消息（我目前只能假设这个是按照书中讲的实现的）
         */
        orderAggregateEventPublisher.publish(order, orderAndEvents.events);

        OrderDetails orderDetails = new OrderDetails(consumerId,
                restaurantId,
                orderLineItems,
                order.getOrderTotal());

        CreateOrderSagaState data = new CreateOrderSagaState(order.getId(), orderDetails);
        sagaInstanceFactory.create(createOrderSaga, data);

//        meterRegistry.ifPresent(mr -> {
//            mr.counter("place_orders").increment();
//        });

        return order;
    }

    private List<OrderLineItem> makeOrderLineItems(List<MenuItemIdAndQuantity> lineItems, Restaurant restaurant) {
        return lineItems.stream()
                .map(li -> {
                    MenuItem om = restaurant
                            .findMenuItem(li.getMenuItemId())
                            .orElseThrow(() -> new InvalidMenuItemIdException(li.getMenuItemId()));
                    return new OrderLineItem(li.getQuantity(), li.getMenuItemId(), om.getName(), om.getPrice());
                }).collect(toList());
    }

    private Order updateOrder(long orderId, Function<Order, List<OrderDomainEvent>> updater) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    orderAggregateEventPublisher.publish(order, updater.apply(order));
                    return order;
                })
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public void confirmRevision(long orderId, OrderRevision revision) {
        updateOrder(orderId, order -> order.confirmRevision(revision));
    }

    public void approveOrder(long orderId) {
        updateOrder(orderId, Order::noteApproved);
//        meterRegistry.ifPresent(mr -> mr.counter("approved_orders").increment());
    }

    public void rejectOrder(long orderId) {
        updateOrder(orderId, Order::noteRejected);
//        meterRegistry.ifPresent(mr -> mr.counter("rejected_orders").increment());
    }

    public void undoPendingRevision(long orderId) {
        updateOrder(orderId, Order::rejectRevision);
    }

    public Optional<RevisedOrder> beginReviseOrder(long orderId, OrderRevision revision) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    ResultWithDomainEvents<LineItemQuantityChange, OrderDomainEvent> result = order.revise(revision);
                    orderAggregateEventPublisher.publish(order, result.events);
                    return new RevisedOrder(order, result.result);
                });
    }

    public void beginCancel(long orderId) {
        updateOrder(orderId, Order::cancel);
    }

    public void undoCancel(long orderId) {
        updateOrder(orderId, Order::undoPendingCancel);
    }

    public void confirmCancelled(long orderId) {
        updateOrder(orderId, Order::noteCancelled);
    }

//    @Transactional
//    public Order reviseOrder(long orderId, OrderRevision orderRevision) {
//        Order order = orderRepository
//                .findById(orderId)
//                .orElseThrow(() -> new OrderNotFoundException(orderId));
//
//        ReviseOrderSagaData sagaData = new ReviseOrderSagaData(
//                order.getConsumerId(),
//                orderId,
//                null,
//                orderRevision);
//
//        sagaInstanceFactory.create(reviseOrderSaga, sagaData);
//    }

}
