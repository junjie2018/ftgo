package fun.junjie.ftgo.orderservice.domain;

import fun.junjie.ftgo.common.Money;
import fun.junjie.ftgo.common.RevisedOrderLineItem;
import fun.junjie.ftgo.orderservice.api.OrderLineItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderLineItems {

    @ElementCollection
    @CollectionTable(name = "order_line_items")
    private List<OrderLineItem> lineItems;

    OrderLineItem findOrderLineItem(String lineItemId) {
        return lineItems.stream()
                .filter(li -> li.getMenuItemId().equals(lineItemId))
                .findFirst()
                .get();
    }

    Money changeToOrderTotal(OrderRevision orderRevision) {
        return orderRevision.getRevisedOrderLineItems().stream()
                .map(item -> {
                    OrderLineItem lineItem = findOrderLineItem(item.getMenuItemId());
                    return lineItem.deltaForChangeQuantity(item.getQuantity());
                })
                .reduce(Money.ZERO, Money::add);
    }

    void updateLineItems(OrderRevision orderRevision) {
        getLineItems().stream()
                .forEach(li -> {
                    Optional<Integer> revised = orderRevision.getRevisedOrderLineItems().stream()
                            .filter(item -> Objects.equals(li.getMenuItemId(), item.getMenuItemId()))
                            .map(RevisedOrderLineItem::getQuantity)
                            .findFirst();

                    li.setQuantity(revised.orElseThrow(() ->
                            new IllegalArgumentException(String.format("menu item id %s not found.", li.getMenuItemId()))));
                });
    }

    Money orderTotal() {
        return lineItems.stream()
                .map(OrderLineItem::getTotal)
                .reduce(Money.ZERO, Money::add);
    }

    LineItemQuantityChange lineItemQuantityChange(OrderRevision orderRevision) {
        Money currentOrderTotal = orderTotal();
        Money delta = changeToOrderTotal(orderRevision);
        Money newOrderTotal = currentOrderTotal.add(delta);

        return new LineItemQuantityChange(currentOrderTotal, newOrderTotal, delta);
    }
}
