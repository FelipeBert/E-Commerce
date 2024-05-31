package org.FelipeBert.ecommerce.domain.repository;

import org.FelipeBert.ecommerce.domain.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
