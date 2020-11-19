package fun.junjie.ftgo.orderservice.sagaparticipants;

import fun.junjie.ftgo.kitchenservice.api.ConfirmCreateTicket;
import fun.junjie.ftgo.kitchenservice.api.CreateTicket;
import fun.junjie.ftgo.kitchenservice.api.CreateTicketReply;
import fun.junjie.ftgo.kitchenservice.api.KitchenServiceChannels;
import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;

public class KitchenServiceProxy {
    public final CommandEndpoint<CreateTicket> create = CommandEndpointBuilder
            .forCommand(CreateTicket.class)
            .withChannel(KitchenServiceChannels.COMMAND_CHANNEL)
            .withReply(CreateTicketReply.class)
            .build();

    public final CommandEndpoint<ConfirmCreateTicket> confirmCreate = CommandEndpointBuilder
            .forCommand(ConfirmCreateTicket.class)
            .withChannel(KitchenServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();

    public final CommandEndpoint<ConfirmCreateTicket> cancel = CommandEndpointBuilder
            .forCommand(ConfirmCreateTicket.class)
            .withChannel(KitchenServiceChannels.COMMAND_CHANNEL)
            .withReply(Success.class)
            .build();
}
