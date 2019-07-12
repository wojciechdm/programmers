package com.wojciechdm.programmers.user.registration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class UserLoginService {

  private UserLoginDao userLoginDao;
  private UserLoginMapper userLoginMapper;

  UserLoginDisplayDto fetchByUsername(String username) {

    return userLoginDao
        .fetchByUsername(username)
        .map(userLoginMapper::toDisplayDto)
        .orElseThrow(
            () ->
                new UserLoginNotFoundException(
                    "UserLogin with username: " + username + " not found"));
  }

  UserLoginDisplayDto fetchById(long id) {

    return userLoginDao
        .fetchById(id)
        .map(userLoginMapper::toDisplayDto)
        .orElseThrow(
            () -> new UserLoginNotFoundException("UserLogin with id: " + id + " not found"));
  }
}
