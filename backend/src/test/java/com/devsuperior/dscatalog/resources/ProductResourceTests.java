package com.devsuperior.dscatalog.resources;


import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService service;
    private ProductDTO dto;

    private PageImpl<ProductDTO> page;

    private Product product;
    private long idExisting;
    private long idNotExisting;
    private long dependentId;


    @BeforeEach
    void setUp() throws Exception {
        dto = Factory.createdProductDTO();
        product = Factory.createdProduct();
        page = new PageImpl<>(List.of(dto));
        idExisting = 1L;
        idNotExisting = 2L;
        dependentId = 3L;


        when(service.findAllPaged(any())).thenReturn(page);

        when(service.findById(idExisting)).thenReturn(dto);
        when(service.findById(idNotExisting)).thenThrow(ResourceNotFoundException.class);

        when(service.update(eq(idExisting), any())).thenReturn(dto);
        when(service.update(eq(idExisting), any())).thenThrow(ResourceNotFoundException.class);

        doNothing().when(service).delete(idExisting);
        doThrow(ResourceNotFoundException.class).when(service).delete(idNotExisting);
        doThrow(DatabaseException.class).when(service).delete(dependentId);

        when(service.insert(any())).thenReturn(dto);
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
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void finByIdShouldReturnNotFoundWhenDoesNotExisting() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", idNotExisting));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);
        ResultActions result = mockMvc.perform(put("/products/{id}", idExisting)
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        Assertions.assertNotNull(result);
    }


    @Test
    public void deleteShouldReturnNoContentWhenIdExisting() throws Exception {
        ResultActions result = mockMvc.perform(delete("/products/{id}", idExisting));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExisting() throws Exception {
        ResultActions result = mockMvc.perform(delete("/products/{id}", idNotExisting));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(dto);

        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
    }
}