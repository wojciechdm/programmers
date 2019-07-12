package com.wojciechdm.programmers.user.registration;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
class Address {

  private Long id;
  private String city;
  private String street;
  private String number;
  private String zipcode;
  private String country;
}
