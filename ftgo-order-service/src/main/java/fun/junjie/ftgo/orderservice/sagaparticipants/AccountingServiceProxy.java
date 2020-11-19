package fun.junjie.ftgo.orderservice.sagaparticipants;

import fun.junjie.ftgo.accountservice.api.AccountingServiceChannels;
import fun.junjie.ftgo.accountservice.api.AuthorizeCommand;
import io.eventuate.tram.commands.common.Success;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import io.eventuate.tram.sagas.simpledsl.CommandEndpointBuilder;

public class AccountingServiceProxy {

    public final CommandEndpoint<AuthorizeCommand> authorize = CommandEndpointBuilder
            .forCommand(AuthorizeCommand.class)
            .withChannel(AccountingServiceChannels.accountingServiceChannel)
            .withReply(Success.class)
            .build();

}
