package ru.misis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.misis.skills.dto.NewSkillDto;
import ru.misis.skills.dto.SkillDto;
import ru.misis.skills.dto.SkillMapper;
import ru.misis.skills.model.Skill;
import ru.misis.user.dto.NewUserDto;
import ru.misis.user.dto.UserDto;
import ru.misis.user.dto.UserMapper;
import ru.misis.user.model.User;

@Service
@RequiredArgsConstructor
public class MapperService {
    private final UserMapper userMapper;
    private final SkillMapper skillMapper;

    public User toUser(NewUserDto userDto) {
        return userMapper.toUser(userDto);
    }

    public UserDto toUserDto(User user) {
        return userMapper.toUserDto(user);
    }

    public Skill toSkill(NewSkillDto skillDto) {
        return skillMapper.toSkill(skillDto);
    }

    public SkillDto toSkillDto(Skill skill) {
        return skillMapper.toSkillDto(skill);
    }
}
