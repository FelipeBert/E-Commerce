package org.FelipeBert.ecommerce.service;

import org.FelipeBert.ecommerce.dto.CartDTO;
import org.FelipeBert.ecommerce.domain.model.Cart;

public interface CartService extends CrudService<Long, Cart>{
    Cart create(CartDTO cartDTO);
    Cart update(Long id, CartDTO cartDTO);
}
