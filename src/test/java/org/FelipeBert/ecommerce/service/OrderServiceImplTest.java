package org.FelipeBert.ecommerce.service;

import org.FelipeBert.ecommerce.domain.model.Order;
import org.FelipeBert.ecommerce.domain.model.Product;
import org.FelipeBert.ecommerce.domain.model.User;
import org.FelipeBert.ecommerce.domain.repository.OrderRepository;
import org.FelipeBert.ecommerce.domain.repository.ProductRepository;
import org.FelipeBert.ecommerce.domain.repository.UserRepository;
import org.FelipeBert.ecommerce.dto.CartItemDTO;
import org.FelipeBert.ecommerce.dto.OrderDTO;
import org.FelipeBert.ecommerce.dto.OrderItemDTO;
import org.FelipeBert.ecommerce.service.exception.IdsMustBeEqualsException;
import org.FelipeBert.ecommerce.service.exception.NotFoundException;
import org.FelipeBert.ecommerce.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    private OrderServiceImpl orderService;

    private Order order;

    private User user;

    private Product product;

    private OrderItemDTO orderItemDTO;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderRepository, userRepository, productRepository);
        order = new Order();
        order.setId(1L);

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);

        orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(1L);
        orderItemDTO.setQuantity(2);
        orderItemDTO.setPrice(BigDecimal.valueOf(100));

        order.setUser(user);
    }

    @Test
    public void testFindAll(){
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<Order> orders = orderService.findAll();
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());
    }

    @Test
    public void testFindById(){
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order newOrder = orderService.findById(1L);
        assertNotNull(newOrder);
        assertEquals(1L, newOrder.getId());
    }

    @Test
    public void testCreateOrderSucesseful(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findAllById(anyList())).thenReturn(List.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setItems(List.of(orderItemDTO));
        orderDTO.setUserId(1L);

        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(List.of(orderItemDTO.dtoToOrderItem(product, order)));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.create(orderDTO);

        assertNotNull(createdOrder);
        assertEquals(user, createdOrder.getUser());
        assertEquals(1, createdOrder.getOrderItems().size());
    }

    @Test
    public void test_create_order_user_not_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(1L);
        orderItemDTO.setQuantity(2);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setItems(List.of(orderItemDTO));

        assertThrows(NotFoundException.class, () -> orderService.create(orderDTO));
    }

    @Test
    public void testUpdateOrderSucessful(){
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.update(1L, order);

        assertEquals(1L, updatedOrder.getId());
        assertEquals(user, updatedOrder.getUser());
        verify(orderRepository).save(order);
    }

    @Test
    public void testUpdateIdsDoNotMatch(){
        Long providedId = 1L;
        Long orderId = 2L;
        Order order = new Order();
        order.setId(orderId);

        assertThrows(IdsMustBeEqualsException.class, () -> orderService.update(providedId, order));

        verify(orderRepository, never()).findById(anyLong());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void testUpdateOrderDontExist(){
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.update(1L ,order));
    }

    @Test
    public void testDeleteOrderById(){
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.delete(orderId);

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    public void testDeleteNonExistingId(){
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.delete(orderId));
    }
}
