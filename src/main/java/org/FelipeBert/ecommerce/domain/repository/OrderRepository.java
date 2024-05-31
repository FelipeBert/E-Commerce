package org.FelipeBert.ecommerce.domain.repository;

import org.FelipeBert.ecommerce.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
