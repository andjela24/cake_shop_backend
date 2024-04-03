package com.andjela.diplomski.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
