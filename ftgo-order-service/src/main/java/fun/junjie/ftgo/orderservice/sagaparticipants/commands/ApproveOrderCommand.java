package fun.junjie.ftgo.orderservice.sagaparticipants.commands;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApproveOrderCommand extends OrderCommand {

    public ApproveOrderCommand(long orderId) {
        super(orderId);
    }

}
