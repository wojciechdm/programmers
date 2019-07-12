package com.wojciechdm.programmers.controllers.rest.registration;

import static org.springframework.http.HttpStatus.CREATED;

import com.wojciechdm.programmers.controllers.security.*;
import com.wojciechdm.programmers.user.registration.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping(path = "/api")
@Slf4j
public class RegistrationController {

  private final UserServiceFacade userServiceFacade;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;

  @ResponseStatus(CREATED)
  @PostMapping(path = "/register")
  public UserDisplayDto save(@RequestBody UserSaveDto userSaveDto) {

    char[] rawPassword = userSaveDto.getUserLogin().getPassword();
    StringBuilder password = new StringBuilder().append(rawPassword);

    userSaveDto.getUserLogin().setPassword(passwordEncoder.encode(password).toCharArray());

    Arrays.fill(rawPassword, '0');
    password.delete(0, password.length());

    UserDisplayDto userSaved = userServiceFacade.save(userSaveDto);
    log.info(
        "User firstName: "
            + userSaved.getFirstName()
            + ", lastName: "
            + userSaved.getLastName()
            + " saved");

    return userSaved;
  }

  @PostMapping("/login")
  public JwtAuthenticationResponse login(@RequestBody UserLoginSaveDto userLoginSaveDto) {

    StringBuilder password = new StringBuilder().append(userLoginSaveDto.getPassword());

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userLoginSaveDto.getUsername(), password));

    Arrays.fill(userLoginSaveDto.getPassword(), '0');
    password.delete(0, password.length());

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    return new JwtAuthenticationResponse(jwt);
  }
}
