package org.FelipeBert.ecommerce.domain.repository;

import org.FelipeBert.ecommerce.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
