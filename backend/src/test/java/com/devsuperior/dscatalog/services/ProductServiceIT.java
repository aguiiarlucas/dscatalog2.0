package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@Transactional
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
    public void deleteShouldDeleteDeleteResourceWhenIdExisting() throws Exception {

        service.delete(idExisting);
        Assertions.assertEquals(countTotalProduct - 1, repository.count());
    }

    @Test
    public void deleteShouldThrowRecourseNotFoundExceptionWhenIdNotExisting() throws Exception {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(idNotExisting);

        });
    }

    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() throws Exception {

        PageRequest page = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(page);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(0, result.getNumber());
        Assertions.assertEquals(10, result.getSize());
        Assertions.assertEquals(countTotalProduct, result.getTotalElements());

    }

    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExisting() throws Exception {

        PageRequest page = PageRequest.of(50, 10);
        Page<ProductDTO> result = service.findAllPaged(page);
        Assertions.assertTrue(result.isEmpty());

    }
    @Test
    public void findAllPagedShouldReturnOrderedPageWhenSortByName() throws Exception {

        PageRequest page = PageRequest.of(0, 10, Sort.by("name"));
        Page<ProductDTO> result = service.findAllPaged(page);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro",result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer",result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa",result.getContent().get(2).getName());

    }



}
