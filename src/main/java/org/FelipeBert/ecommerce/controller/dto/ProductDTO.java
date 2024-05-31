package org.FelipeBert.ecommerce.controller.dto;

import org.FelipeBert.ecommerce.domain.model.Product;

public record ProductDTO(Long id,
                      String name,
                      String description,
                      double price,
                      String category) {
    public ProductDTO(Product product){
        this(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory());
    }

    public Product dtoToProduct(){
        Product model = new Product();
        model.setId(this.id);
        model.setName(this.name);
        model.setDescription(this.description);
        model.setPrice(this.price);
        model.setCategory(this.category);
        return model;
    }
}
