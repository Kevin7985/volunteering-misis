package ru.misis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.misis.category.dto.CategoryDto;
import ru.misis.category.dto.CategoryMapper;
import ru.misis.category.dto.NewCategoryDto;
import ru.misis.category.model.Category;
import ru.misis.skill.dto.NewSkillDto;
import ru.misis.skill.dto.SkillDto;
import ru.misis.skill.dto.SkillMapper;
import ru.misis.skill.model.Skill;
import ru.misis.user.dto.NewUserDto;
import ru.misis.user.dto.UserDto;
import ru.misis.user.dto.UserMapper;
import ru.misis.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapperService {
    private final UserMapper userMapper;
    private final SkillMapper skillMapper;
    private final CategoryMapper categoryMapper;

    public User toUser(NewUserDto userDto) {
        return userMapper.toUser(userDto);
    }

    public UserDto toUserDto(User user) {
        List<SkillDto> skillDtoList = user.getSkills().stream().map(this::toSkillDto).toList();
        return userMapper.toUserDto(user, skillDtoList);
    }

    public Skill toSkill(NewSkillDto skillDto) {
        return skillMapper.toSkill(skillDto);
    }

    public Skill toSkill(SkillDto skillDto) {
        return skillMapper.toSkill(skillDto);
    }

    public SkillDto toSkillDto(Skill skill) {
        return skillMapper.toSkillDto(skill);
    }

    public Category toCategory(NewCategoryDto categoryDto) {
        return categoryMapper.toCategory(categoryDto);
    }

    public CategoryDto toCategoryDto(Category category) {
        return categoryMapper.toCategoryDto(category);
    }
}
