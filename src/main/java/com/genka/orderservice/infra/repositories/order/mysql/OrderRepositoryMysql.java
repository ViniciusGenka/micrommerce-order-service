package com.genka.orderservice.infra.repositories.order.mysql;

import com.genka.orderservice.domain.entities.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepositoryMysql extends JpaRepository<Order, UUID> {
    List<Order> findByBuyerEmailAddress(String buyerEmailAddress);
}
