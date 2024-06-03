package org.FelipeBert.ecommerce.controller;


import org.FelipeBert.ecommerce.dto.OrderDTO;
import org.FelipeBert.ecommerce.domain.model.Order;
import org.FelipeBert.ecommerce.service.OrderService;
import org.FelipeBert.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public record OrderController(OrderService orderService, ProductService productService) {

    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAll(){
        var orders = orderService.findAll();
        var orderToReturn = orders.stream().map(OrderDTO::new).toList();
        return ResponseEntity.ok(orderToReturn);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(new OrderDTO(orderService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody OrderDTO orderDTO){
        Order order = orderService.create(orderDTO);
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody OrderDTO orderDTO){
        Order updateOrder = orderService.update(id, orderDTO);
        return ResponseEntity.ok(orderDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
