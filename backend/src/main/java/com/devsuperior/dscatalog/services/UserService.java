package com.devsuperior.dscatalog.services;


import com.devsuperior.dscatalog.dto.*;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service

public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> page = repository.findAll(pageable);
        return page.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = repository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        copyDtoToEntity(dto, user);
        user = repository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Entity not found with id " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation: " + e.getMessage());
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.getRoles().clear();

        for (RoleDTO roleDTO : dto.getRoles()) {
            Role role = roleRepository.findById(roleDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            entity.getRoles().add(role);
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user = repository.findByEmail(username);
         if(user == null){
             logger.error("User not found "+ username);
             throw  new UsernameNotFoundException("Email not found");
         }
         logger.info("User found"+username);
         return  user;
    }
}
