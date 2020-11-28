package fun.junjie.ftgo.orderservice.sagaparticipants.commands;

import fun.junjie.ftgo.orderservice.sagaparticipants.commands.OrderCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RejectOrderCommand extends OrderCommand {

    public RejectOrderCommand(long orderId) {
        super(orderId);
    }

}
