package org.FelipeBert.ecommerce.controller;

import org.FelipeBert.ecommerce.controller.dto.ProductDTO;
import org.FelipeBert.ecommerce.domain.model.Product;
import org.FelipeBert.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public record ProductController(ProductService productService) {

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll(){
        var products = productService.findAll();
        var productsDTO = products.stream().map(ProductDTO::new).toList();
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(new ProductDTO(productService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO productDTO){
        Product createdProduct = productDTO.dtoToProduct();
        productService.create(createdProduct);
        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        Product updatedProduct = productDTO.dtoToProduct();
        productService.update(id, updatedProduct);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
