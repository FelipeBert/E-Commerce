package org.FelipeBert.ecommerce.service;

import org.FelipeBert.ecommerce.dto.CartDTO;
import org.FelipeBert.ecommerce.dto.CartItemDTO;
import org.FelipeBert.ecommerce.domain.model.Cart;
import org.FelipeBert.ecommerce.domain.model.Product;
import org.FelipeBert.ecommerce.domain.model.User;
import org.FelipeBert.ecommerce.domain.repository.CartRepository;
import org.FelipeBert.ecommerce.domain.repository.ProductRepository;
import org.FelipeBert.ecommerce.domain.repository.UserRepository;
import org.FelipeBert.ecommerce.service.exception.BusinessException;
import org.FelipeBert.ecommerce.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    private CartServiceImpl cartService;

    private Cart cart;

    private User user;

    private Product product;

    private CartItemDTO cartItemDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartServiceImpl(cartRepository, productRepository, userRepository);
        cart = new Cart();

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);

        cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(1L);
        cartItemDTO.setQuantity(2);
        cartItemDTO.setPrice(BigDecimal.valueOf(100));

        cart.setId(1L);
    }

    @Test
    public void testFindAll(){
        when(cartRepository.findAll()).thenReturn(List.of(cart));

        List<Cart> carts = cartRepository.findAll();
        assertNotNull(carts);
        assertEquals(1, carts.size());
        assertEquals(1L, carts.get(0).getId());
    }

    @Test
    public void testFindById(){
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        Cart foundCart = cartService.findById(1L);
        assertNotNull(foundCart);
        assertEquals(1L, foundCart.getId());
    }

    @Test
    public void testCreateCartSucess(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findAllById(anyList())).thenReturn(List.of(product));

        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(1L);
        cartDTO.setItems(List.of(cartItemDTO));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.addItem(cartItemDTO.dtoToCartItem(product, cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart createdCart = cartService.create(cartDTO);

        assertNotNull(createdCart);
        assertEquals(1, createdCart.getItemList().size());
    }

    @Test
    public void testCreateCartFailure(){
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        Cart newCart = new Cart();
        Exception exception = assertThrows(BusinessException.class, () -> cartService.update(1L, newCart));

        String expectedMessage = "Cart not found with id: " + 1L;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateExistingCartWithItens(){
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(1L);
        cartDTO.setItems(List.of(cartItemDTO));

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findAllById(anyList())).thenReturn(List.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.update(1L, cartDTO);

        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getItemList().size());
        assertEquals(1L, updatedCart.getItemList().get(0).getProduct().getId());
    }

    @Test
    public void testUpdateCartNotFound(){
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(1L);

        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        CartServiceImpl cartService = new CartServiceImpl(cartRepository, productRepository, userRepository);
        Exception exception = assertThrows(BusinessException.class, () -> cartService.update(1L, cartDTO));

        String expectedMessage = "Cart not found with id: " + 1L;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testDeleteCart(){
        Long cartId = 1L;
        when(cartRepository.existsById(cartId)).thenReturn(true);

        cartService.delete(cartId);

        verify(cartRepository, times(1)).deleteById(cartId);
    }

    @Test
    public void testDeleteCartNotFound(){
        Long cartId = 1L;
        when(cartRepository.existsById(cartId)).thenReturn(false);

        Exception exception = assertThrows(BusinessException.class, () -> cartService.delete(cartId));

        String expectedMessage = "Cart not found with id: " + cartId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
