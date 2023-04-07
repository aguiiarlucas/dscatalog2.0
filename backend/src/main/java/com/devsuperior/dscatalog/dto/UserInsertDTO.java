package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@UserInsertValid
public class UserInsertDTO extends UserDTO {
    private String password;

    public UserInsertDTO() {
        super();
    }
}
