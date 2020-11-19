package fun.junjie.ftgo.orderservice.api.events;

import fun.junjie.ftgo.common.Address;
import fun.junjie.ftgo.orderservice.api.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent implements OrderDomainEvent {
    private OrderDetails orderDetails;
    private Address deliveryAddress;
    private String restaurantName;
}
