package org.FelipeBert.ecommerce.domain.repository;

import org.FelipeBert.ecommerce.domain.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
