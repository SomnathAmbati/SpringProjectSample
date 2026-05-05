package com.example.SpringProject.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	@NotNull(message="{user.name.invalid}")
	private String name;
	@NotNull(message="{user.email.invalid}")
	@Email(message="{user.email.structure}")
	private String email;
	@NotNull(message= "{user.password.invalid}")
	private String password;

}
