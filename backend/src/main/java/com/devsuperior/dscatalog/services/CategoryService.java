package com.devsuperior.dscatalog.services;


import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repositoy;
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repositoy.findAll();
            return list.stream().map(CategoryDTO::new).toList();

    }
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id ){
        Optional<Category> obj =repositoy.findById(id);
        var entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

    @Transactional(readOnly = true)
    public CategoryDTO insert(CategoryDTO dto) {
        var entity = new Category();
        entity.setName(dto.getName());
        entity = repositoy.save(entity);
        return new CategoryDTO(entity);

    }
}
