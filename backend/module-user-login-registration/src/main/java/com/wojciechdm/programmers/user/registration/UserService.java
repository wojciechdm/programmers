package com.wojciechdm.programmers.user.registration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class UserService {

  private UserDao userDao;
  private UserMapper userMapper;

  UserDisplayDto save(UserSaveDto userSaveDto) {
    return userMapper.toDisplayDto(userDao.save(userMapper.toEntity(userSaveDto)));
  }
}
