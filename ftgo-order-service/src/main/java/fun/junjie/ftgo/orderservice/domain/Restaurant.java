package fun.junjie.ftgo.orderservice.domain;

import fun.junjie.ftgo.kitchenservice.api.TicketDetails;
import fun.junjie.ftgo.orderservice.api.events.OrderDomainEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "order_service_restaurants")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class Restaurant {

    @Id
    private Long id;

    @Embedded
    @ElementCollection
    @CollectionTable(name = "order_service_restaurant_menu_items")
    private List<MenuItem> menuItems;

    private String name;

    public List<OrderDomainEvent> reviseMenu(List<MenuItem> revisedMenu) {
        throw new UnsupportedOperationException();
    }

    public void verifyRestaurantDetails(TicketDetails ticketDetails) {
        // todo implement me
    }

    public Optional<MenuItem> findMenuItem(String menuItemId) {
        return menuItems.stream()
                .filter(mi -> mi.getId().equals(menuItemId))
                .findFirst();
    }
}
