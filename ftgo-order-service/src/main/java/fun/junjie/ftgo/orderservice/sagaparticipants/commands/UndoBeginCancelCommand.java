package fun.junjie.ftgo.orderservice.sagaparticipants.commands;

public class UndoBeginCancelCommand extends OrderCommand {

    public UndoBeginCancelCommand(long orderId) {
        super(orderId);
    }

}
