package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.EmptyStackException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private long idExisting;
    private long nonExisting;


    @BeforeEach
    void setUp() throws Exception {
        idExisting = 1L;
        nonExisting=1000L;
        Mockito.doNothing().when(repository).deleteById(idExisting);
        Mockito.doThrow(EmptyStackException.class).when(repository).deleteById(nonExisting);
    }

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;

    @Test
    public void deleteShouldDoNothingWhenIdExisting() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(idExisting);
        });
        Mockito.verify(repository).deleteById(idExisting);
    }
}
