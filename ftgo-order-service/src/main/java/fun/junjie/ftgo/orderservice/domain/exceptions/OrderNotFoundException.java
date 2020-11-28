package fun.junjie.ftgo.orderservice.domain.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long orderId) {
        super("Order not found " + orderId);
    }
}
