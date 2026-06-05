package by.vstu.shop.repository;

import by.vstu.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //заказы конкретного пользователя
    List<Order> findByUserId(Long userId);

    //заказы по статусу
    List<Order> findByStatus(Order.EStatus status);

    //заказы пользователя по статусу
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = :status")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId,
                                      @Param("status") Order.EStatus status);
}
