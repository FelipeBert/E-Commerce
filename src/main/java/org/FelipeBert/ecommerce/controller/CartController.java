package org.FelipeBert.ecommerce.controller;

import org.FelipeBert.ecommerce.dto.CartDTO;
import org.FelipeBert.ecommerce.domain.model.Cart;
import org.FelipeBert.ecommerce.service.CartService;
import org.FelipeBert.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public record CartController(CartService cartService, ProductService productService) {

    @GetMapping
    public ResponseEntity<List<CartDTO>> findAll(){
        var cart = cartService.findAll();
        var cartsDTO = cart.stream().map(CartDTO::new).toList();
        return ResponseEntity.ok(cartsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(new CartDTO(cartService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CartDTO> create(@RequestBody CartDTO cartDTO){
        Cart cart = cartService.create(cartDTO);
        return ResponseEntity.ok(new CartDTO(cart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartDTO> update(@PathVariable Long id, @RequestBody CartDTO cartDTO){
        Cart updatedCart = cartService.update(id, cartDTO);
        return ResponseEntity.ok(new CartDTO(updatedCart));
    }
}
