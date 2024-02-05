package ru.misis.user.dto;

import org.springframework.stereotype.Component;
import ru.misis.user.model.User;

@Component
public class UserMapper {
    public User toUser(NewUserDto userDto) {
        return new User(
                null,
                userDto.getMisisId(),
                userDto.getEmail(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getMiddleName()
        );
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getMisisId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName()
        );
    }
}
