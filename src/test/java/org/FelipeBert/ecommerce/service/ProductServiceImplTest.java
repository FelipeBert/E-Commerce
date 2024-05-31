package org.FelipeBert.ecommerce.service;

import org.FelipeBert.ecommerce.domain.model.Product;
import org.FelipeBert.ecommerce.domain.repository.ProductRepository;
import org.FelipeBert.ecommerce.service.exception.EntityAlreadyExistsException;
import org.FelipeBert.ecommerce.service.exception.IdsMustBeEqualsException;
import org.FelipeBert.ecommerce.service.exception.NotFoundException;
import org.FelipeBert.ecommerce.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
        product = new Product();
        product.setId(1L);
        product.setPrice(10);
        product.setName("product");
        product.setDescription("description");
        product.setCategory("category");
    }

    @Test
    public void testFindAll(){
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> products = productService.findAll();
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("product", products.get(0).getName());
        assertEquals(10, products.get(0).getPrice());
        assertEquals("description", products.get(0).getDescription());
    }

    @Test
    public void testFindById(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product foundProduct = productService.findById(1L);
        assertNotNull(foundProduct);
        assertEquals(1L, foundProduct.getId());
    }

    @Test
    public void testFindByIdNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    public void testCreateProductSucess(){
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.create(product);
        assertNotNull(createdProduct);
        assertEquals("description", product.getDescription());
    }

    @Test
    public void testCreateProductAlreadyExists(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(EntityAlreadyExistsException.class, () -> productService.create(product));
    }

    @Test
    public void testUpdateProductSucess(){
        Product newValues = new Product();
        newValues.setId(1L);
        newValues.setDescription("newDescription");
        newValues.setName("newName");
        newValues.setCategory("newCategory");
        newValues.setPrice(15);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(newValues);
        Product updatedProduct = productService.update(1L, newValues);

        assertEquals("newDescription", updatedProduct.getDescription());
        assertEquals("newName", updatedProduct.getName());
        assertEquals("newCategory", updatedProduct.getCategory());
        assertEquals(15, updatedProduct.getPrice());
    }

    @Test
    public void testUpdateProductNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.update(1L, product));
    }

    @Test
    public void testUpdateProductIdsNotEqual(){
        Product newProduct = new Product();
        newProduct.setId(2L);
        assertThrows(IdsMustBeEqualsException.class, () -> productService.update(1L, newProduct));
    }

    @Test
    public void testDeleteProduct(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertDoesNotThrow(() -> productService.delete(1L));
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    public void testDeleteProductNotFound(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> productService.delete(1L));
    }

}
