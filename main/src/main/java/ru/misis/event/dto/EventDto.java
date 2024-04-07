package ru.misis.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.misis.category.dto.CategoryDto;
import ru.misis.event.model.EventState;
import ru.misis.skill.dto.SkillDto;
import ru.misis.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EventDto {
    private final UUID id;
    private final String title;
    private final String description;
    private final UserDto creator;
    private final CategoryDto category;
    private final List<SkillDto> skills;
    private final String location;
    private final String picture;
    private final LocalDateTime startDate;
    private final LocalDateTime finishDate;
    private final Integer participantLimit;
    private final EventState state;
    private final LocalDateTime createdAt;
}
