package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    Set<RoleDTO> roles = new HashSet<>();

    public  UserDTO(){
        super();
    }


    public UserDTO(User entity) {
        this.id= entity.getId();
        this.firstName= entity.getFirstName();
        this.lastName=entity.getLastName();
        this.email=entity.getEmail();
        entity.getRoles().forEach(role->this.roles.add(new RoleDTO(role)));
    }


}
