package ru.misis.user;

import ru.misis.user.dto.NewUserDto;
import ru.misis.user.dto.UpdateUserDto;
import ru.misis.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(NewUserDto userDto);

    List<UserDto> findUsers(String email, Integer from, Integer size);

    UserDto getUserById(UUID id);

    UserDto updateUserById(UUID id, UpdateUserDto userDto);

    void deleteUserById(UUID id);
}