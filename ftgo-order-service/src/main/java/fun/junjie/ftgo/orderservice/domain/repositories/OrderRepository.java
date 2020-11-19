package fun.junjie.ftgo.orderservice.domain.repositories;

import fun.junjie.ftgo.orderservice.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
