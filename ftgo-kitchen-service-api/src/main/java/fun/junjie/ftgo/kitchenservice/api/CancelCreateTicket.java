package fun.junjie.ftgo.kitchenservice.api;

import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelCreateTicket implements Command {
    private Long ticketId;
}
