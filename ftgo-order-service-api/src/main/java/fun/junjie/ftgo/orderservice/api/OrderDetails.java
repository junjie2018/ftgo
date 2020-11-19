package fun.junjie.ftgo.orderservice.api;

import fun.junjie.ftgo.common.Money;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {

    private long consumerId;
    private long restaurantId;


    private List<OrderLineItem> lineItems;

    @Setter(AccessLevel.NONE)
    private Money orderTotal;
}
