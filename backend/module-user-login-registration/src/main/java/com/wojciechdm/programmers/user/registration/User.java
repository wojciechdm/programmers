package com.wojciechdm.programmers.user.registration;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
class User {

  private Long id;
  private UserLogin userLogin;
  private String firstName;
  private String lastName;
  private Integer age;
  private LocalDate dateOfBirth;
  private Address address;
  private String title;
}
