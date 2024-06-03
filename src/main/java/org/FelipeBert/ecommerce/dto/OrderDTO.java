package org.FelipeBert.ecommerce.dto;

import org.FelipeBert.ecommerce.domain.model.Order;
import org.FelipeBert.ecommerce.domain.model.Product;
import org.FelipeBert.ecommerce.domain.model.User;
import org.FelipeBert.ecommerce.service.exception.BusinessException;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {
    private Long id;
    private Long userId;
    private List<OrderItemDTO> items;

    public OrderDTO(){}

    public OrderDTO(Long id, Long userId, List<OrderItemDTO> items) {
        this.id = id;
        this.userId = userId;
        this.items = items;
    }

    public OrderDTO(Order order){
        this(order.getId(), order.getUser().getId(), order.getOrderItems()
                .stream()
                .map(OrderItemDTO::new).collect(Collectors.toList()));
    }

    public Order dtoToOrder(User user, List<Product> products){
        Order newOrder = new Order();
        newOrder.setId(this.id);
        newOrder.setUser(user);
        newOrder.setOrderItems(this.items.stream().map(orderItemDTO -> {
            Product product = products.stream().filter(o -> o.getId().equals(orderItemDTO.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Product not found with id: " + orderItemDTO.getProductId()));
            return orderItemDTO.dtoToOrderItem(product, newOrder);}
        ).collect(Collectors.toList()));
        return newOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}
