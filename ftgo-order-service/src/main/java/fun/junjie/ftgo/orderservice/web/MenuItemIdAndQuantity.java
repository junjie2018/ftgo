package fun.junjie.ftgo.orderservice.web;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class MenuItemIdAndQuantity {

    private String menuItemId;

    @Setter(AccessLevel.NONE)
    private int quantity;

}
