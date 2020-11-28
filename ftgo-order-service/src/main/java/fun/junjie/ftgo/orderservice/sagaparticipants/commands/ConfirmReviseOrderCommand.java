package fun.junjie.ftgo.orderservice.sagaparticipants.commands;

import fun.junjie.ftgo.orderservice.domain.OrderRevision;
import lombok.Getter;

@Getter
public class ConfirmReviseOrderCommand extends OrderCommand {

    private OrderRevision revision;

    protected ConfirmReviseOrderCommand() {

    }

    public ConfirmReviseOrderCommand(long orderId, OrderRevision orderRevision) {
        super(orderId);

        this.revision = orderRevision;
    }
}
