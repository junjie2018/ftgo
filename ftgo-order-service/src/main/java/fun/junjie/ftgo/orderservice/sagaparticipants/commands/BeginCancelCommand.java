package fun.junjie.ftgo.orderservice.sagaparticipants.commands;

public class BeginCancelCommand extends OrderCommand {

    private BeginCancelCommand() {

    }

    public BeginCancelCommand(long orderId) {
        super(orderId);
    }

}
