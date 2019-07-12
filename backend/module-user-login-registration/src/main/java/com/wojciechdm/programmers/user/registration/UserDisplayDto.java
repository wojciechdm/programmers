package com.wojciechdm.programmers.user.registration;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDisplayDto {

    private String firstName;
    private String lastName;
    private Integer age;
    private LocalDate dateOfBirth;
    private AddressDisplayDto address;
    private String title;
}
