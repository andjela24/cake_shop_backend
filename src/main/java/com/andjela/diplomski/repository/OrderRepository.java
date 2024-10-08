package com.andjela.diplomski.repository;

import com.andjela.diplomski.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> getUsersOrders(@Param("userId") Long userId);

    Order findByPaymentId(String paymentId);

}
