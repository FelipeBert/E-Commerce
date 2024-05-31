package org.FelipeBert.ecommerce.dto;

import org.FelipeBert.ecommerce.domain.model.Cart;
import org.FelipeBert.ecommerce.domain.model.Product;
import org.FelipeBert.ecommerce.domain.model.User;
import org.FelipeBert.ecommerce.service.exception.BusinessException;

import java.util.List;
import java.util.stream.Collectors;

public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> items;

    public CartDTO(Long id, Long userId, List<CartItemDTO> items) {
        this.id = id;
        this.userId = userId;
        this.items = items;
    }

    public CartDTO(Cart cart) {
        this(cart.getId(), cart.getUser().getId(), cart.getItemList().stream().map(CartItemDTO::new).collect(Collectors.toList()));
    }

    public CartDTO() {

    }

    public Cart dtoToCart(User user, List<Product> products) {
        Cart cart = new Cart();
        cart.setId(this.id);
        cart.setUser(user);
        cart.setItemList(this.items.stream().map(itemDTO -> {
            Product product = products.stream()
                    .filter(p -> p.getId().equals(itemDTO.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Product not found with id: " + itemDTO.getProductId()));
            return itemDTO.dtoToCartItem(product, cart);
        }).collect(Collectors.toList()));
        return cart;
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

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }
}
