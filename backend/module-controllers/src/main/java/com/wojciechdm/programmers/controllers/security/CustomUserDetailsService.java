package com.wojciechdm.programmers.controllers.security;

import com.wojciechdm.programmers.user.registration.UserLoginDisplayDto;
import com.wojciechdm.programmers.user.registration.UserLoginServiceFacade;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserLoginServiceFacade userLoginServiceFacade;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserLoginDisplayDto userLogin = userLoginServiceFacade.fetchByUsername(username);

    return UserPrincipal.create(userLogin);
  }

  UserDetails loadUserById(Long id) {
    UserLoginDisplayDto userLogin = userLoginServiceFacade.fetchById(id);

    return UserPrincipal.create(userLogin);
  }
}
