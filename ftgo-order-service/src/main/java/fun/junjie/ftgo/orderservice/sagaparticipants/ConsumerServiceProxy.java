package fun.junjie.ftgo.orderservice.sagaparticipants;

import fun.junjie.ftgo.consumerservice.api.ConsumerServiceChannels;
import fun.junjie.ftgo.consumerservice.api.ValidateOrderByConsumer;
import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;

public class ConsumerServiceProxy {
    public final CommandEndpoint<ValidateOrderByConsumer> validateOrder = CommandEndpointBuilder
            .forCommand(ValidateOrderByConsumer.class)
            .withChannel(ConsumerServiceChannels.consumerServiceChannel)
            .withReply(Success.class)
            .build();
}
