package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    private long idExisting;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @BeforeEach
    void setUp() {
        idExisting = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createdProduct();
        page = new PageImpl<>(List.of(product));

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(ArgumentMatchers.any())).thenReturn(product);
        when(repository.findById(idExisting)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        doNothing().when(repository).deleteById(idExisting);
        doThrow(EmptyStackException.class).when(repository).deleteById(nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
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
        verify(repository).deleteById(idExisting);
    }

//    @Test
//    public void deleteShouldThrowResourceNotFoundExceptionNothingWhenDependentId() {
//        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
//            service.delete(nonExistingId);
//        });
//        Mockito.verify(repository, Mockito.times(1))
//                .deleteById(nonExistingId);
//    }

    @Test
    public void deleteShouldThrowDataBaseExceptionNothingWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
        verify(repository).deleteById(dependentId);


    }
}




