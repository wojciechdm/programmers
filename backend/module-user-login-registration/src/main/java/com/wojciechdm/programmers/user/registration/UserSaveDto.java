package com.wojciechdm.programmers.user.registration;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class UserSaveDto {

    private UserLoginSaveDto userLogin;
    private String firstName;
    private String lastName;
    private Integer age;
    private LocalDate dateOfBirth;
    private AddressSaveDto address;
    private String title;
}
