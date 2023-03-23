package com.devsuperior.dscatalog.resources;


import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService service;
    private ProductDTO dto;
    private PageImpl<ProductDTO> page;

    private Product product;
    private long idExisting;
    private long idNotExisting;


    @BeforeEach
    void setUp() throws Exception {
        dto = Factory.createdProductDTO();
        product = Factory.createdProduct();
        page = new PageImpl<>(List.of(dto));
        idExisting = 1L;
        idNotExisting = 10000L;


        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(idExisting)).thenReturn(dto);
        when(service.findById(idNotExisting)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products"));
    }

    @Test
    public void finByIdShouldReturnProductWhenIdExisting() throws Exception {

        ResultActions result = mockMvc.perform(get("/products/{id}", idExisting));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void finByIdShouldReturnNotFoundWhenDoesNotExisting() throws Exception {

        ResultActions result = mockMvc.perform(get("/products/{id}", idNotExisting));
        result.andExpect(status().isNotFound());
    }

}
