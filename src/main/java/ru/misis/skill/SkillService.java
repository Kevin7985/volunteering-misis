package ru.misis.skill;

import ru.misis.skill.dto.NewSkillDto;
import ru.misis.skill.dto.SkillDto;
import ru.misis.skill.dto.UpdateSkillDto;

import java.util.List;
import java.util.UUID;

public interface SkillService {
    SkillDto addSkill(NewSkillDto skillDto);

    List<SkillDto> searchSkills(String title, Integer from, Integer size);

    SkillDto getSkillById(UUID id);

    SkillDto updateSkillById(UUID id, UpdateSkillDto skillDto);

    void deleteSkillById(UUID id);
}
