package fun.junjie.ftgo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevisedOrderLineItem {
    private int quantity;
    private String menuItemId;
}
