package ru.misis.skills.dto;

import org.springframework.stereotype.Component;
import ru.misis.skills.model.Skill;

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
