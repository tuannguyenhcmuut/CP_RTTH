package org.ut.server.omsserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ut.server.omsserver.model.OrderHistory;

import java.util.List;

//@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    // get order histories by order id
    List<OrderHistory> findAllByOrderId(Long orderId);
    // get order histories by order id and sort by action date
    List<OrderHistory> findAllByOrderIdOrderByActionDateDesc(Long orderId);
    // find all order history by order code
    /*
    * SELECT description FROM order_history oh
JOIN "order" o
ON o.id = oh.id
WHERE o.code = 'ORDER-91641'

    * */
    @Query(value = "SELECT action_date, description FROM order_history oh JOIN \"order\" o ON o.id = oh.order_id WHERE o.code = :orderCode", nativeQuery = true)
    List<Object[]> findOrderHistoryInfoByOrderCode(@Param("orderCode") String orderCode);
}
