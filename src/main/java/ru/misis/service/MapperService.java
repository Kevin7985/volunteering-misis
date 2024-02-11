package ru.misis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.misis.categories.dto.CategoryDto;
import ru.misis.categories.dto.CategoryMapper;
import ru.misis.categories.dto.NewCategoryDto;
import ru.misis.categories.model.Category;
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
    private final CategoryMapper categoryMapper;

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

    public Category toCategory(NewCategoryDto categoryDto) {
        return categoryMapper.toCategory(categoryDto);
    }

    public CategoryDto toCategoryDto(Category category) {
        return categoryMapper.toCategoryDto(category);
    }
}
