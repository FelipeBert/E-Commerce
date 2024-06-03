package org.FelipeBert.ecommerce.service.impl;

import org.FelipeBert.ecommerce.dto.OrderDTO;
import org.FelipeBert.ecommerce.dto.OrderItemDTO;
import org.FelipeBert.ecommerce.domain.model.Order;
import org.FelipeBert.ecommerce.domain.model.Product;
import org.FelipeBert.ecommerce.domain.model.User;
import org.FelipeBert.ecommerce.domain.repository.OrderRepository;
import org.FelipeBert.ecommerce.domain.repository.ProductRepository;
import org.FelipeBert.ecommerce.domain.repository.UserRepository;
import org.FelipeBert.ecommerce.service.OrderService;
import org.FelipeBert.ecommerce.service.exception.IdsMustBeEqualsException;
import org.FelipeBert.ecommerce.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Order create(Order order){
        return orderRepository.save(order);
    }

    @Transactional
    public Order create(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(NotFoundException::new);
        List<Product> products = productRepository.findAllById(
                orderDTO.getItems().stream().map(OrderItemDTO::getProductId).collect(Collectors.toList())
        );
        Order order = orderDTO.dtoToOrder(user, products);
        return orderRepository.save(order);
    }

    @Transactional
    public Order update(Long id, OrderDTO orderDTO){
        Order existOrder = orderRepository.findById(orderDTO.getId())
                .orElseThrow(NotFoundException::new);

        User existUser = userRepository.findById(orderDTO.getUserId()).
                orElseThrow(NotFoundException::new);

        List<Product> products = productRepository.findAllById(orderDTO.getItems()
                .stream().map(OrderItemDTO::getProductId).collect(Collectors.toList()));

        Order updateOrder = orderDTO.dtoToOrder(existUser, products);
        updateOrder.setId(existOrder.getId());

        existOrder.getOrderItems().clear();
        existOrder.getOrderItems().addAll(updateOrder.getOrderItems());

        return orderRepository.save(existOrder);
    }

    @Transactional
    public Order update(Long id,Order order) {
        if(!id.equals(order.getId())){
            throw new IdsMustBeEqualsException();
        }
        Optional<Order> optionalOrder = orderRepository.findById(id);
        Order updateOrder = optionalOrder.orElseThrow(NotFoundException::new);
        updateOrder.setUser(order.getUser());
        updateOrder.setOrderItems(order.getOrderItems());
        return orderRepository.save(updateOrder);
    }

    @Transactional
    public void delete(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        Order orderToDelete = optionalOrder.orElseThrow(NotFoundException::new);
        orderRepository.delete(orderToDelete);
    }
}
