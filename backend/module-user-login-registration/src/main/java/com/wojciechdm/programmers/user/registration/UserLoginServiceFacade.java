package com.wojciechdm.programmers.user.registration;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

public class UserLoginServiceFacade {

  private UserLoginService userLoginService;

  public UserLoginServiceFacade(UserLoginDao userLoginDao) {
    this.userLoginService = new UserLoginService(userLoginDao, new UserLoginMapper());
  }

  public UserLoginDisplayDto fetchByUsername(String username) {

    checkArgument(nonNull(username), "Username can not be null");

    return userLoginService.fetchByUsername(username);
  }

  public UserLoginDisplayDto fetchById(long id) {

    return userLoginService.fetchById(id);
  }
}
