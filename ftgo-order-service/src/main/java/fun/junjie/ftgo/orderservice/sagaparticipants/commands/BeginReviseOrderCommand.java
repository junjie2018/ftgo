package fun.junjie.ftgo.orderservice.sagaparticipants.commands;

import fun.junjie.ftgo.orderservice.domain.OrderRevision;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeginReviseOrderCommand extends OrderCommand {

    private OrderRevision revision;

    private BeginReviseOrderCommand() {

    }

    public BeginReviseOrderCommand(long orderId, OrderRevision orderRevision) {
        super(orderId);
        this.revision = orderRevision;
    }
}
