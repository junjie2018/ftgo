package fun.junjie.ftgo.orderservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RevisedOrder {

    private final Order order;
    private final LineItemQuantityChange change;

}
