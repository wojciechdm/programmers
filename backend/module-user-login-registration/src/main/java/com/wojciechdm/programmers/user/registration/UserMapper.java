package com.wojciechdm.programmers.user.registration;

class UserMapper {

  UserDisplayDto toDisplayDto(User user) {
    return new UserDisplayDto(
        user.getFirstName(),
        user.getLastName(),
        user.getAge(),
        user.getDateOfBirth(),
        new AddressDisplayDto(
            user.getAddress().getCity(),
            user.getAddress().getStreet(),
            user.getAddress().getNumber(),
            user.getAddress().getZipcode(),
            user.getAddress().getCountry()),
        user.getTitle());
  }

  User toEntity(UserSaveDto userSaveDto) {
    return new User(
        null,
        new UserLogin(
            null,
            userSaveDto.getUserLogin().getUsername(),
            new String(userSaveDto.getUserLogin().getPassword()),
            null,
            UserRole.USER),
        userSaveDto.getFirstName(),
        userSaveDto.getLastName(),
        userSaveDto.getAge(),
        userSaveDto.getDateOfBirth(),
        new Address(
            null,
            userSaveDto.getAddress().getCity(),
            userSaveDto.getAddress().getStreet(),
            userSaveDto.getAddress().getNumber(),
            userSaveDto.getAddress().getZipcode(),
            userSaveDto.getAddress().getCountry()),
        userSaveDto.getTitle());
  }
}
