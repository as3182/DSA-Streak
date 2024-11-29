package com.streak.dsastreak.entity;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@vrv(user|admin)$",
            message = "Username must end with @vrvuser or @vrvadmin.")
    private String userName;
    private String password;
    private String name;
}
