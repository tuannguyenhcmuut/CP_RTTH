package org.ut.server.omsserver.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ut.server.omsserver.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.shopOwner.id = :userId")
    Optional<Order> findOrderByIdAndShopOwner_Id(Long orderId, UUID userId);

    List<Order> findOrdersByShopOwner_Id(UUID userId);
    List<Order> findOrdersByShopOwner_Id(UUID userId, Pageable pageable);

    Optional<Order> findByIdAndShopOwner_Id(Long orderId, UUID userId);

    // native query by user id "SELECT r.name AS receiver_name, SUM(op.total_items_price) AS total_spending
    //FROM receiver r
    //JOIN "order" o ON r.receiver_id = o.receiver_id
    //JOIN order_price op ON o.price_id = op.id
    //WHERE o.user_id = 'db67ebe4-260e-4164-bec7-ec5da22e5326'
    //GROUP BY r.name
    //ORDER BY total_spending DESC
    //LIMIT 10;"

    @Query(value = "SELECT r.name AS receiver_name, SUM(op.total_items_price) AS total_spending " +
            "FROM receiver r " +
            "JOIN \"order\" o ON r.receiver_id = o.receiver_id " +
            "JOIN order_price op ON o.price_id = op.id " +
            "WHERE o.user_id = :userId " +
            "GROUP BY r.name " +
            "ORDER BY total_spending DESC " +
            "LIMIT 10;", nativeQuery = true)
    List<Object[]> findTopReceivers(@Param("userId") UUID userId);

//    SELECT TO_CHAR(o.created_date, 'YYYY-MM') AS month_with_year, COUNT(o.id) AS total_orders, SUM(op.total_items_price) AS total_spending
//    FROM "order" o
//    JOIN order_price op ON o.price_id = op.id
//    WHERE o.user_id = '05b7493c-8cce-4d10-a4b2-2528c678c954'
//    GROUP BY month_with_year
//    ORDER BY month_with_year;

    @Query(value = "SELECT TO_CHAR(o.created_date, 'YYYY-MM') AS month_with_year, COUNT(o.id) AS total_orders, SUM(op.total_items_price) AS total_spending " +
            "FROM \"order\" o " +
            "JOIN order_price op ON o.price_id = op.id " +
            "WHERE o.user_id = :userId " +
            "GROUP BY month_with_year " +
            "ORDER BY month_with_year;", nativeQuery = true)
    List<Object[]> findStatistics(UUID userId);

    @Query(value = "SELECT COUNT(o) FROM \"order\" o WHERE o.user_id = :userId AND DATE(o.created_date) = CURRENT_DATE", nativeQuery = true)
    Long countTotalOrderCreatedToday(@Param("userId") UUID userId);

    @Query(value = "SELECT COUNT(o) FROM \"order\" o WHERE o.user_id = :userId AND DATE(o.last_updated_date) = CURRENT_DATE AND o.status_code = 'DELIVERED'", nativeQuery = true)
    Long countTotalOrderDeliveredToday(@Param("userId") UUID userId);

    // calculate total revenue of shop owner
    @Query(value = "SELECT SUM(op.collection_charge) FROM \"order\" o JOIN order_price op ON o.price_id = op.id WHERE o.user_id = :userId", nativeQuery = true)
    Double calculateTotalRevenue(@Param("userId") UUID userId);

    // calculate total received money of shop owner

}
