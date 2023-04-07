package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.services.validation.UserInsertValid;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    @NotBlank(message = "Campo obrigatório")
    private String firstName;
    private String lastName;
    @Email(message = "Favor , entrar com e-mail valido")
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
