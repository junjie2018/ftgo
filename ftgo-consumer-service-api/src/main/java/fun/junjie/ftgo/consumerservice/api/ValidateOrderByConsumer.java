package fun.junjie.ftgo.consumerservice.api;

import fun.junjie.ftgo.common.Money;
import io.eventuate.tram.commands.common.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateOrderByConsumer implements Command {
    private long consumerId;
    private long orderId;
    private Money orderTotal;
}
