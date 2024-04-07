package ru.misis.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.misis.category.dto.CategoryDto;
import ru.misis.event.exceptions.EventValidation;
import ru.misis.event.model.EventState;
import ru.misis.skill.dto.SkillDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class UpdateEventDto {
    private String title;
    private String description;
    private CategoryDto category;
    private List<SkillDto> skills;
    private String location;
    private String picture;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private Integer participantLimit;
    private EventState state;

    public UpdateEventDto(String title, String description, CategoryDto category, List<SkillDto> skills, String location, String picture, LocalDateTime startDate, LocalDateTime finishDate, Integer participantLimit, EventState state) {
        if (title != null && (title.isEmpty() || title.length() > 255)) {
            throw new EventValidation("Название мероприятия не может быть короче 1 символа и длинее 255");
        }
        this.title = title;

        if (description != null && description.length() > 5000) {
            throw new EventValidation("Описание события не может быть длинее 5000 символов");
        }
        this.description = description;

        this.category = category;
        this.skills = skills;
        this.location = location;
        this.picture = picture;

        if (startDate != null && startDate.isBefore(LocalDateTime.now())) {
            throw new EventValidation("Дата начала не может быть раньше текущего момента");
        }
        this.startDate = startDate;

        if (finishDate != null && finishDate.isBefore(LocalDateTime.now())) {
            throw new EventValidation("Дата окончания не может быть раньше текущего момента");
        }
        this.finishDate = finishDate;

        this.participantLimit = participantLimit;
        this.state = state;
    }
}
