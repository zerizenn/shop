package by.vstu.shop.repository;

import by.vstu.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //эндпоинт 1: фильтр по категории
    List<Product> findByCategoryId(Long categoryId);

    //эндпоинт 2: фильтр по цене (не дороже maxPrice)
    @Query("SELECT p FROM Product p WHERE p.price <= :maxPrice")
    List<Product> findByPriceLessThanEqual(@Param("maxPrice") BigDecimal maxPrice);

    //эндпоинт 3: товары в наличии (quantity > 0)
    @Query("SELECT p FROM Product p WHERE p.quantity > 0")
    List<Product> findAllInStock();
}
