package fun.junjie.ftgo.accountservice.api;

import fun.junjie.ftgo.common.Money;
import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizeCommand implements Command {

    private Long consumerId;
    private Long orderId;
    private Money orderTotal;

}
