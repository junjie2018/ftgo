package fun.junjie.ftgo.orderservice.sagas.createorder;

import fun.junjie.ftgo.accountservice.api.AuthorizeCommand;
import fun.junjie.ftgo.consumerservice.api.ValidateOrderByConsumer;
import fun.junjie.ftgo.kitchenservice.api.*;
import fun.junjie.ftgo.orderservice.api.OrderDetails;
import fun.junjie.ftgo.orderservice.api.OrderLineItem;
import fun.junjie.ftgo.orderservice.sagaparticipants.ApproveOrderCommand;
import fun.junjie.ftgo.orderservice.sagaparticipants.RejectOrderCommand;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Data
@NoArgsConstructor
public class CreateOrderSagaState {

    private Long orderId;
    private long ticketId;

    @Setter(AccessLevel.NONE)
    private OrderDetails orderDetails;

    public CreateOrderSagaState(Long orderId, OrderDetails orderDetails) {
        this.orderId = orderId;
        this.orderDetails = orderDetails;
    }

    // region kitchen service

    CreateTicket makeCreateTicketCommand() {
        return new CreateTicket(
                getOrderDetails().getRestaurantId(),
                getOrderId(),
                makeTicketDetails(getOrderDetails()));
    }

    CancelCreateTicket makeCancelCreateTicketCommand() {
        return new CancelCreateTicket(getTicketId());
    }

    ConfirmCreateTicket makeConfirmCreateTicketCommand() {
        return new ConfirmCreateTicket(getTicketId());
    }

    private TicketDetails makeTicketDetails(OrderDetails orderDetails) {
        return new TicketDetails(makeTicketLineItems(orderDetails.getLineItems()));
    }

    private List<TicketLineItem> makeTicketLineItems(List<OrderLineItem> lineItems) {
        return lineItems.stream().map(this::makeTicketLineItem).collect(toList());
    }

    private TicketLineItem makeTicketLineItem(OrderLineItem orderLineItem) {
        return new TicketLineItem(
                orderLineItem.getMenuItemId(),
                orderLineItem.getName(),
                orderLineItem.getQuantity());
    }


    void handleCreateTicketReply(CreateTicketReply reply) {
        logger.debug("getTicketId{}", reply.getTicketId());
        setTicketId(reply.getTicketId());
    }

    // endregion

    // region order service

    AuthorizeCommand makeAuthorizeCommand() {
        return new AuthorizeCommand(
                getOrderDetails().getConsumerId(),
                getOrderId(),
                getOrderDetails().getOrderTotal());
    }

    ApproveOrderCommand makeApproveOrderCommand() {
        return new ApproveOrderCommand(getOrderId());
    }

    RejectOrderCommand makeRejectOrderCommand() {
        return new RejectOrderCommand(getOrderId());
    }

    // endregion

    // region consumer service

    ValidateOrderByConsumer makeValidateOrderByConsumerCommand() {
        return new ValidateOrderByConsumer(
                getOrderDetails().getConsumerId(),
                getOrderId(),
                getOrderDetails().getOrderTotal());
    }

    // endregion

}
