package fun.junjie.ftgo.orderservice.sagaparticipants;

import io.eventuate.tram.commands.common.Command;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class OrderCommand implements Command {
    private long orderId;
}
