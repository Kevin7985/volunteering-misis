package ru.misis.skill;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.misis.service.MapperService;
import ru.misis.service.ValidationService;
import ru.misis.skill.dto.NewSkillDto;
import ru.misis.skill.dto.SkillDto;
import ru.misis.skill.dto.UpdateSkillDto;
import ru.misis.skill.model.Skill;
import ru.misis.utils.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillServiceImpl implements SkillService {
    private final MapperService mapperService;
    private final ValidationService validationService;
    private final SkillRepository skillRepository;

    @Override
    public SkillDto addSkill(NewSkillDto skillDto) {
        Skill skill = mapperService.toSkill(skillDto);
        skill = skillRepository.save(skill);

        log.info("Содан новый навык: " + skill);
        return mapperService.toSkillDto(skill);
    }

    @Override
    public List<SkillDto> searchSkills(String title, Integer from, Integer size) {
        List<SkillDto> skillsList = new ArrayList<>();

        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Page<Skill> page;
        Pagination pager = new Pagination(from, size);

        for (int i = pager.getPageStart(); i < pager.getPagesAmount(); i++) {
            pageable = PageRequest.of(i, pager.getPageSize(), sort);
            page = title == null ? skillRepository.findAll(pageable) : skillRepository.search(title, pageable);
            skillsList.addAll(page.stream()
                    .map(mapperService::toSkillDto)
                    .toList());
        }

        log.info(String.format("Поиск навыков (title = %s, from = %d, size = %d)", title, from, size));
        return skillsList;
    }

    @Override
    public SkillDto getSkillById(UUID id) {
        Skill skill = validationService.validateSkill(id);

        log.info("Получение навыка по id = " + id);
        return mapperService.toSkillDto(skill);
    }

    @Override
    public SkillDto updateSkillById(UUID id, UpdateSkillDto skillDto) {
        Skill skill = validationService.validateSkill(id);

        skill.setTitle(skillDto.getTitle() == null ? skill.getTitle() : skillDto.getTitle());
        skill = skillRepository.save(skill);

        log.info("Обновлен навык: " + skill);
        return mapperService.toSkillDto(skill);
    }

    @Override
    public void deleteSkillById(UUID id) {
        validationService.validateSkill(id);

        log.info("Удалён навык с id = " + id);
        skillRepository.deleteById(id);
    }
}
