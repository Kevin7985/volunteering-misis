package ru.misis.user;

import org.springframework.security.core.Authentication;
import ru.misis.user.dto.NewUserDto;
import ru.misis.user.dto.UpdateUserDto;
import ru.misis.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto createUser(NewUserDto userDto);

    List<UserDto> findUsers(String email, Integer from, Integer size);

    UserDto getUserById(UUID id);

    UserDto updateUserById(Authentication auth, UUID id, UpdateUserDto userDto);

    void deleteUserById(Authentication auth, UUID id);
}