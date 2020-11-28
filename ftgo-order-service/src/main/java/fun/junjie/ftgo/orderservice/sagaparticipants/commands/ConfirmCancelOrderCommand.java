package fun.junjie.ftgo.orderservice.sagaparticipants.commands;

public class ConfirmCancelOrderCommand extends OrderCommand {

    private ConfirmCancelOrderCommand() {

    }

    public ConfirmCancelOrderCommand(long orderId) {
        super(orderId);
    }

}
