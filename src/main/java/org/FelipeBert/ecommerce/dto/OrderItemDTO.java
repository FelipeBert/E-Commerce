package org.FelipeBert.ecommerce.dto;

import org.FelipeBert.ecommerce.domain.model.Order;
import org.FelipeBert.ecommerce.domain.model.OrderItem;
import org.FelipeBert.ecommerce.domain.model.Product;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Long id;
    private Long productId;
    private int quantity;
    private BigDecimal price;

    public OrderItemDTO(){}

    public OrderItemDTO(OrderItem orderItem){
        this.id = orderItem.getId();
        this.productId = orderItem.getProduct().getId();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }

    public OrderItem dtoToOrderItem(Product product, Order order){
        OrderItem newOrder = new OrderItem();
        newOrder.setId(this.id);
        newOrder.setPrice(this.price);
        newOrder.setOrder(order);
        newOrder.setProduct(product);
        newOrder.setQuantity(this.quantity);
        return newOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
