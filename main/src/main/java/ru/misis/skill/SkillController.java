package ru.misis.skill;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.misis.skill.dto.NewSkillDto;
import ru.misis.skill.dto.SkillDto;
import ru.misis.skill.dto.UpdateSkillDto;
import ru.misis.utils.models.ListResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skills")
@Validated
@RequiredArgsConstructor
@Tag(name = "Skills", description = "Работа с навыками на платформе")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
public class SkillController {
    private final SkillService skillService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового навыка")
    public SkillDto addSkill(@RequestBody @Valid NewSkillDto skillDto) {
        return skillService.addSkill(skillDto);
    }

    @GetMapping
    @Operation(summary = "Поиск навыков по названию")
    public ListResponse<SkillDto> findSkills(
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
    public SkillDto updateSkillById(Authentication auth, @PathVariable UUID id, @RequestBody UpdateSkillDto skillDto) {
        return skillService.updateSkillById(auth, id, skillDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление навыка по идентификатору")
    public void deleteSkillById(Authentication auth, @PathVariable UUID id) {
        skillService.deleteSkillById(auth, id);
    }
}
