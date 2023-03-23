package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
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
    private Category category;
    private ProductDTO dto;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        idExisting = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        product = Factory.createdProduct();
        category= Factory.createdCategory();
        dto=Factory.createdProductDTO();
        page = new PageImpl<>(List.of(product));

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        when(repository.findById(idExisting)).thenReturn(Optional.of(product));
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        when(repository.getOne(idExisting)).thenReturn(product);
        when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);

        when(categoryRepository.getOne(idExisting)).thenReturn(category);
        when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);


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

    @Test
    public void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        verify(repository).findAll(pageable);
    }
    @Test
    public void findByIdShouldReturnProductDTOWhenIdExisting(){
        Assertions.assertDoesNotThrow(()->{
            service.findById(idExisting);
        });
        verify(repository).findById(idExisting);
    }
    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNothingExisting(){

        Assertions.assertThrows(ResourceNotFoundException.class,()->{
           service.findById(nonExistingId);
        });
        verify(repository).findById(nonExistingId);
    }
    @Test
    public void updateReturnProductDTOWhenIdExisting(){

       ProductDTO result =  service.update(idExisting,dto);
        Assertions.assertNotNull(result);
    }
    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdNothingExisting(){

        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            service.update(nonExistingId,dto);
        });

    }
}




