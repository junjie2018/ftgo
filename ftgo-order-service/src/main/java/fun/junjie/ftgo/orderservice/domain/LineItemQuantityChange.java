package fun.junjie.ftgo.orderservice.domain;

import fun.junjie.ftgo.common.Money;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LineItemQuantityChange {

    final Money currentOrderTotal;
    final Money newOrderTotal;
    final Money delta;

}
