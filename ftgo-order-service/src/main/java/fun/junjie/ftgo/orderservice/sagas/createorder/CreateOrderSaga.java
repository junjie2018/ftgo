package fun.junjie.ftgo.orderservice.sagas.createorder;

import fun.junjie.ftgo.kitchenservice.api.CreateTicketReply;
import fun.junjie.ftgo.orderservice.sagaparticipants.AccountingServiceProxy;
import fun.junjie.ftgo.orderservice.sagaparticipants.ConsumerServiceProxy;
import fun.junjie.ftgo.orderservice.sagaparticipants.KitchenServiceProxy;
import fun.junjie.ftgo.orderservice.sagaparticipants.OrderServiceProxy;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateOrderSaga implements SimpleSaga<CreateOrderSagaState> {

    private SagaDefinition<CreateOrderSagaState> sagaDefinition;

    @Override
    public SagaDefinition<CreateOrderSagaState> getSagaDefinition() {
        return sagaDefinition;
    }

    public CreateOrderSaga(
            OrderServiceProxy orderService,
            ConsumerServiceProxy consumerService,
            KitchenServiceProxy kitchenService,
            AccountingServiceProxy accountingService) {

        // @formatter:off

        this.sagaDefinition =
                step()
                    .withCompensation(
                        orderService.reject,
                        CreateOrderSagaState::makeRejectOrderCommand)

                .step()
                    .invokeParticipant(
                        consumerService.validateOrder,
                        CreateOrderSagaState::makeValidateOrderByConsumerCommand)

                .step()
                    .invokeParticipant(
                        kitchenService.create,
                        CreateOrderSagaState::makeCreateTicketCommand)
                    .onReply(
                        CreateTicketReply.class,
                        CreateOrderSagaState::handleCreateTicketReply)
                    .withCompensation(
                        kitchenService.cancel,
                        CreateOrderSagaState::makeConfirmCreateTicketCommand)

                .step()
                    .invokeParticipant(
                            accountingService.authorize,
                            CreateOrderSagaState::makeAuthorizeCommand)

                .step()
                    .invokeParticipant(
                            kitchenService.confirmCreate,
                            CreateOrderSagaState::makeConfirmCreateTicketCommand)

                .step()
                    .invokeParticipant(
                            orderService.approve,
                            CreateOrderSagaState::makeApproveOrderCommand)

                .build();

        // @formatter:off
    }
}
