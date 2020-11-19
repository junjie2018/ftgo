package fun.junjie.ftgo.orderservice.domain;

import fun.junjie.ftgo.orderservice.api.OrderDetails;
import fun.junjie.ftgo.orderservice.api.events.OrderDomainEvent;
import fun.junjie.ftgo.orderservice.api.OrderLineItem;
import fun.junjie.ftgo.orderservice.domain.exceptions.InvalidMenuItemIdException;
import fun.junjie.ftgo.orderservice.domain.exceptions.RestaurantNotFoundException;
import fun.junjie.ftgo.orderservice.domain.repositories.OrderRepository;
import fun.junjie.ftgo.orderservice.domain.repositories.RestaurantRespository;
import fun.junjie.ftgo.orderservice.sagas.createorder.CreateOrderSaga;
import fun.junjie.ftgo.orderservice.sagas.createorder.CreateOrderSagaState;
import fun.junjie.ftgo.orderservice.web.MenuItemIdAndQuantity;
import io.eventuate.tram.events.aggregates.ResultWithDomainEvents;
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class OrderService {

    private RestaurantRespository restaurantRespository;
    private OrderRepository orderRepository;

    private OrderDomainEventPublisher orderAggregateEventPublisher;

    private SagaInstanceFactory sagaInstanceFactory;

    private CreateOrderSaga createOrderSaga;

    @Transactional
    public Order createOrder(
            long consumerId,
            long restaurantId,
            DeliveryInformation deliveryInformation,
            List<MenuItemIdAndQuantity> lineItems) {

        Restaurant restaurant = restaurantRespository
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        List<OrderLineItem> orderLineItems = makeOrderLineItems(lineItems, restaurant);

        ResultWithDomainEvents<Order, OrderDomainEvent> orderAndEvents =
                Order.createOrder(consumerId, restaurant, deliveryInformation, orderLineItems);

        Order order = orderAndEvents.result;
        orderRepository.save(order);

        orderAggregateEventPublisher.publish(order, orderAndEvents.events);

        OrderDetails orderDetails = new OrderDetails(
                consumerId, restaurantId, orderLineItems, order.getOrderTotal());

        CreateOrderSagaState data = new CreateOrderSagaState(order.getId(), orderDetails);
        sagaInstanceFactory.create(createOrderSaga, data);

//        meterRegistry.ifPresent(mr -> {
//            mr.counter("place_orders").increment();
//        });

        return order;
    }

    private List<OrderLineItem> makeOrderLineItems(List<MenuItemIdAndQuantity> lineItems, Restaurant restaurant) {
        return lineItems.stream()
                .map(li -> {
                    MenuItem om = restaurant
                            .findMenuItem(li.getMenuItemId())
                            .orElseThrow(() -> new InvalidMenuItemIdException(li.getMenuItemId()));
                    return new OrderLineItem(li.getQuantity(), li.getMenuItemId(), om.getName(), om.getPrice());
                }).collect(toList());
    }

}
