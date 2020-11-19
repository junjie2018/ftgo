package fun.junjie.ftgo.orderservice.domain.repositories;

import fun.junjie.ftgo.orderservice.domain.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantRespository extends CrudRepository<Restaurant, Long> {
}
