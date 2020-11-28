package fun.junjie.ftgo.orderservice.sagaparticipants.commands;

public class UndoBeginReviseOrderCommand extends OrderCommand {

    protected UndoBeginReviseOrderCommand() {

    }

    public UndoBeginReviseOrderCommand(long orderId) {
        super(orderId);
    }
}
