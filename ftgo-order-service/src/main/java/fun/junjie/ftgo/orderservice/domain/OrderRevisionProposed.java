package fun.junjie.ftgo.orderservice.domain;

import fun.junjie.ftgo.common.Money;
import fun.junjie.ftgo.orderservice.api.events.OrderDomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRevisionProposed implements OrderDomainEvent {
    private final OrderRevision orderRevision;
    private final Money currentOrderTotal;
    private final Money newOrderTotal;
}
