package fun.junjie.ftgo.kitchenservice.api;

import lombok.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketLineItem {
    private String menuItemId;
    private String name;
    private int quantity;
}
