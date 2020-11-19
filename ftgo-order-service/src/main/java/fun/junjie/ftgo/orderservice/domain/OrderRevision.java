package fun.junjie.ftgo.orderservice.domain;

import fun.junjie.ftgo.common.RevisedOrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderRevision {

    private Optional<DeliveryInformation> deliveryInformation = Optional.empty();
    private List<RevisedOrderLineItem> revisedOrderLineItems;

}
