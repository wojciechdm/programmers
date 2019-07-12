package com.wojciechdm.programmers.user.registration;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

public class UserServiceFacade {

  private UserService userService;

  public UserServiceFacade(UserDao userDao) {
    userService = new UserService(userDao, new UserMapper());
  }

  public UserDisplayDto save(UserSaveDto userSaveDto) {

    checkArgument(nonNull(userSaveDto), "User can not be null");

    return userService.save(userSaveDto);
  }
}
