package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserInsertValid;
import com.devsuperior.dscatalog.services.validation.UserUpdateValid;
import lombok.AllArgsConstructor;
import lombok.Data;


@UserUpdateValid
public class UserUpdateDTO extends UserDTO {

}
