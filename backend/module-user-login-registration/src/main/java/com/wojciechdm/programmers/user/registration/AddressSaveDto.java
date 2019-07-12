package com.wojciechdm.programmers.user.registration;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressSaveDto {

  private String city;
  private String street;
  private String number;
  private String zipcode;
  private String country;
}
