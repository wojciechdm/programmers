package com.wojciechdm.programmers.user.registration;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class UserLoginDisplayDto {

    private Long id;
    private String username;
    private String password;
    private Long userDataId;
    private UserRole role;
}
