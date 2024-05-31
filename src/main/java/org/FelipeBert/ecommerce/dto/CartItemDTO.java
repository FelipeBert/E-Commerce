package org.FelipeBert.ecommerce.dto;

import org.FelipeBert.ecommerce.domain.model.Cart;
import org.FelipeBert.ecommerce.domain.model.CartItem;
import org.FelipeBert.ecommerce.domain.model.Product;

import java.math.BigDecimal;

public class CartItemDTO {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;

    public CartItemDTO() {
    }

    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getId();
        this.quantity = cartItem.getQuantity();
        this.price = cartItem.getPrice();
    }

    public CartItem dtoToCartItem(Product product, Cart cart) {
        CartItem cartItem = new CartItem();
        cartItem.setId(this.id);
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItem.setQuantity(this.quantity);
        cartItem.setPrice(this.price);
        return cartItem;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
