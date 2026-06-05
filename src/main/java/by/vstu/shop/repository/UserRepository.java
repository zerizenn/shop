package by.vstu.shop.repository;

import by.vstu.shop.dto.UserDetailsDTO;
import by.vstu.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("""
            SELECT new by.vstu.shop.dto.UserDetailsDTO(
                u.id,
                u.username,
                COUNT(o.id),
                COALESCE(SUM(o.totalAmount), 0)
            )
            FROM User u
            LEFT JOIN Order o ON o.user = u AND o.deleted = false
            WHERE u.id = :userId
            GROUP BY u.id, u.username
            """)
    Optional<UserDetailsDTO> findUserDetails(@Param("userId") Long userId);
}
