package ru.misis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.misis.user.dto.NewUserDto;
import ru.misis.user.dto.UserDto;
import ru.misis.user.dto.UserMapper;
import ru.misis.user.model.User;

@Service
@RequiredArgsConstructor
public class MapperService {
    private final UserMapper userMapper;

    public User toUser(NewUserDto userDto) {
        return userMapper.toUser(userDto);
    }

    public UserDto toUserDto(User user) {
        return userMapper.toUserDto(user);
    }
}
