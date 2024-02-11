package ru.misis.skills;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.misis.skills.dto.NewSkillDto;
import ru.misis.skills.dto.SkillDto;
import ru.misis.skills.dto.UpdateSkillDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skills")
@Validated
@RequiredArgsConstructor
@Tag(name = "Skills", description = "Работа с навыками на платформе")
@SecurityRequirement(name = "Bearer Authentication")
public class SkillController {
    private final SkillService skillService;

    @PostMapping
    @Operation(summary = "Создание нового навыка")
    public SkillDto addSkill(@RequestBody @Valid NewSkillDto skillDto) {
        return skillService.addSkill(skillDto);
    }

    @GetMapping
    @Operation(summary = "Поиск навыков по названию")
    public List<SkillDto> findSkills(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return skillService.searchSkills(title, from, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение навыка по идентификатору")
    public SkillDto getSkillById(@PathVariable UUID id) {
        return skillService.getSkillById(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление навыка по идентификатору")
    public SkillDto updateSkillById(@PathVariable UUID id, @RequestBody UpdateSkillDto skillDto) {
        return skillService.updateSkillById(id, skillDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление навыка по идентификатору")
    public void deleteSkillById(@PathVariable UUID id) {
        skillService.deleteSkillById(id);
    }
}
