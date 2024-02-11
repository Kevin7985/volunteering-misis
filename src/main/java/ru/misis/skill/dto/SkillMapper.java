package ru.misis.skill.dto;

import org.springframework.stereotype.Component;
import ru.misis.skill.model.Skill;

@Component
public class SkillMapper {
    public Skill toSkill(NewSkillDto skillDto) {
        return new Skill(
                null,
                skillDto.getTitle()
        );
    }

    public SkillDto toSkillDto(Skill skill) {
        return new SkillDto(
                skill.getId(),
                skill.getTitle()
        );
    }
}
