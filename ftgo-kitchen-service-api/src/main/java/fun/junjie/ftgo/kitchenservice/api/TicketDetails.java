package fun.junjie.ftgo.kitchenservice.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetails {
    private List<TicketLineItem> lineItems;

}
