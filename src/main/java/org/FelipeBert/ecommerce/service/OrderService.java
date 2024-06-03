package org.FelipeBert.ecommerce.service;

import org.FelipeBert.ecommerce.dto.OrderDTO;
import org.FelipeBert.ecommerce.domain.model.Order;

public interface OrderService extends CrudService<Long, Order> {
    Order create(OrderDTO orderDTO);
    Order update(Long id, OrderDTO orderDTO);
}
