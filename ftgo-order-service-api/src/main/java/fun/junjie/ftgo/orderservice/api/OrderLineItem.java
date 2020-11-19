package fun.junjie.ftgo.orderservice.api;


import fun.junjie.ftgo.common.Money;
import lombok.*;

import javax.persistence.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItem {
    private int quantity;
    private String menuItemId;
    private String name;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "amount", column = @Column(name = "price")))
    private Money price;

    public Money deltaForChangeQuantity(int newQuantity) {
        return price.multiply(newQuantity - quantity);
    }

    public Money getTotal() {
        return price.multiply(quantity);
    }
}
