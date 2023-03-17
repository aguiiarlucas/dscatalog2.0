package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long exitingId;
    private long nonExistingId;
    private long countTotalProduct;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        exitingId = 1L;
        nonExistingId = 10000L;
        countTotalProduct =25L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        repository.deleteById(exitingId);
        Optional<Product> result = repository.findById(exitingId);
        Assertions.assertFalse(result.isPresent());
    }
    @Test
    public void finByIdShouldReturnNomEmptyOptionalWhenIdExists() {

        Optional<Product> result = repository.findById(exitingId);
        Assertions.assertTrue(result.isPresent());
    }
    @Test
    public void finByIdShouldReturnNomEmptyOptionalWhenIdDoesNonExists() {

        Optional<Product> result = repository.findById(nonExistingId);
        Assertions.assertTrue(result.isEmpty());
    }
    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);

        });
    }
    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull(){

        Product product = Factory.createdProduct();
        product.setId(null);
        repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProduct+1,product.getId());
    }


}
