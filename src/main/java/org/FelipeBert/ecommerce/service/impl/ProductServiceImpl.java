package org.FelipeBert.ecommerce.service.impl;

import org.FelipeBert.ecommerce.domain.model.Product;
import org.FelipeBert.ecommerce.domain.repository.ProductRepository;
import org.FelipeBert.ecommerce.service.ProductService;
import org.FelipeBert.ecommerce.service.exception.EntityAlreadyExistsException;
import org.FelipeBert.ecommerce.service.exception.IdsMustBeEqualsException;
import org.FelipeBert.ecommerce.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository itemRepository) {
        this.productRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public Product create(Product entity) {
        Optional<Product> checkProduct = productRepository.findById(entity.getId());
        if(checkProduct.isPresent()){
            throw new EntityAlreadyExistsException("Product");
        }
        Product createdItem = new Product();
        createdItem.setCategory(entity.getCategory());
        createdItem.setDescription(entity.getDescription());
        createdItem.setName(entity.getName());
        createdItem.setPrice(entity.getPrice());
        return productRepository.save(createdItem);
    }

    @Transactional
    public Product update(Long id, Product entity) {
        if(!id.equals(entity.getId())){
            throw new IdsMustBeEqualsException();
        }
        Optional<Product> checkItemExists = productRepository.findById(entity.getId());
        Product updatedProduct = checkItemExists.orElseThrow(NotFoundException::new);
        updatedProduct.setPrice(entity.getPrice());
        updatedProduct.setCategory(entity.getCategory());
        updatedProduct.setDescription(entity.getDescription());
        updatedProduct.setName(entity.getName());
        return productRepository.save(updatedProduct);
    }

    @Transactional
    public void delete(Long id) {
        Optional<Product> checkItemExists = productRepository.findById(id);
        Product itemToDelete = checkItemExists.orElseThrow(NotFoundException::new);
        productRepository.delete(itemToDelete);
    }

}
