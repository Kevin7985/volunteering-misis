package ru.misis.user.dto;

import org.springframework.stereotype.Component;
import ru.misis.skill.dto.SkillDto;
import ru.misis.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public User toUser(NewUserDto userDto) {
        return new User(
                null,
                userDto.getMisisId(),
                userDto.getEmail(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getMiddleName(),
                "",
                "",
                false,
                false,
                new ArrayList<>()
        );
    }

    public UserDto toUserDto(User user, List<SkillDto> skills) {
        return new UserDto(
                user.getId(),
                user.getMisisId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                "",
                user.getAvatar(),
                user.getIsModerator(),
                user.getIsStaff(),
                skills
        );
    }
}
