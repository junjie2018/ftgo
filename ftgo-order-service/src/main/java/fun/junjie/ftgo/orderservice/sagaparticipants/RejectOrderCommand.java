package fun.junjie.ftgo.orderservice.sagaparticipants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RejectOrderCommand extends OrderCommand {

    public RejectOrderCommand(long orderId) {
        super(orderId);
    }

}
