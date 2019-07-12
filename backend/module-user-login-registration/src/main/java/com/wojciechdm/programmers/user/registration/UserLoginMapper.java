package com.wojciechdm.programmers.user.registration;

class UserLoginMapper {

  UserLoginDisplayDto toDisplayDto(UserLogin userlogin) {
    return new UserLoginDisplayDto(
        userlogin.getId(),
        userlogin.getUsername(),
        userlogin.getPassword(),
        userlogin.getUserDataId(),
        userlogin.getRole());
  }
}
