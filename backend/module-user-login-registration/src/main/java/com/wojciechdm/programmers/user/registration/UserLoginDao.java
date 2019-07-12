package com.wojciechdm.programmers.user.registration;

import java.util.Optional;

interface UserLoginDao {

    Optional<UserLogin> fetchByUsername(String username);

    Optional<UserLogin> fetchById(long id);
}
