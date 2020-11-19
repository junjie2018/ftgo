package fun.junjie.ftgo.kitchenservice.api;

import io.eventuate.tram.commands.CommandDestination;
import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@CommandDestination("restaurantService")
public class CreateTicket implements Command {
    private Long orderId;
    private long restaurantId;
    private TicketDetails ticketDetails;
}
