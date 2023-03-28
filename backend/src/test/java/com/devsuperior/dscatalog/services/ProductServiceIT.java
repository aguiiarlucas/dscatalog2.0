package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductServiceIT {

    @Autowired
    private ProductService service;
    @Autowired
    private ProductRepository repository;

    private Long idExisting;
    private Long idNotExisting;
    private Long countTotalProduct;

    @BeforeEach
    void setUp() throws Exception {
        idExisting = 1L;
        idNotExisting = 10000L;
        countTotalProduct = 25L;

    }

    @Test
    public void deleteShouldDeleteDeleteResourceWhenIdExisting()throws Exception{

        service.delete(idExisting);
        Assertions.assertEquals(countTotalProduct - 1 ,repository.count());
    }
    @Test
    public void deleteShouldThrowRecourseNotFoundExceptionWhenIdNotExisting()throws Exception{

        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            service.delete(idNotExisting);

        });
    }
}
