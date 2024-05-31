package org.FelipeBert.ecommerce.service.impl;

import org.FelipeBert.ecommerce.dto.CartDTO;
import org.FelipeBert.ecommerce.dto.CartItemDTO;
import org.FelipeBert.ecommerce.domain.model.Cart;
import org.FelipeBert.ecommerce.domain.model.Product;
import org.FelipeBert.ecommerce.domain.model.User;
import org.FelipeBert.ecommerce.domain.repository.CartRepository;
import org.FelipeBert.ecommerce.domain.repository.ProductRepository;
import org.FelipeBert.ecommerce.domain.repository.UserRepository;
import org.FelipeBert.ecommerce.service.CartService;
import org.FelipeBert.ecommerce.service.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//Todo : Implement Methods to Insert, Update, Delete products in the cart
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cart not found with id: " + id));
    }

    @Override
    @Transactional
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart create(CartDTO cartDTO) {
        User user = userRepository.findById(cartDTO.getUserId())
                .orElseThrow(() -> new BusinessException("User not found with id: " + cartDTO.getUserId()));
        List<Product> products = productRepository.findAllById(
                cartDTO.getItems().stream().map(CartItemDTO::getProductId).collect(Collectors.toList())
        );
        Cart cart = cartDTO.dtoToCart(user, products);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public Cart update(Long id, Cart cart) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cart not found with id: " + id));
        existingCart.getItemList().clear();
        existingCart.getItemList().addAll(cart.getItemList());
        return cartRepository.save(existingCart);
    }

    @Override
    @Transactional
    public Cart update(Long id, CartDTO cartDTO) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cart not found with id: " + id));
        User user = userRepository.findById(cartDTO.getUserId())
                .orElseThrow(() -> new BusinessException("User not found with id: " + cartDTO.getUserId()));
        List<Product> products = productRepository.findAllById(
                cartDTO.getItems().stream().map(CartItemDTO::getProductId).collect(Collectors.toList())
        );
        Cart updatedCart = cartDTO.dtoToCart(user, products);
        updatedCart.setId(existingCart.getId());
        existingCart.getItemList().clear();
        existingCart.getItemList().addAll(updatedCart.getItemList());
        return cartRepository.save(existingCart);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new BusinessException("Cart not found with id: " + id);
        }
        cartRepository.deleteById(id);
    }
}
