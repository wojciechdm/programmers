package com.wojciechdm.programmers.user.registration;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class UserLoginSaveDto {

  private String username;
  private char[] password;
}
