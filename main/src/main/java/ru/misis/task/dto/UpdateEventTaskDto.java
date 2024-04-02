package ru.misis.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.misis.task.exceptions.EventTaskValidation;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UpdateEventTaskDto {
    private UUID executorId;
    private String title;
    private String description;
    private Boolean isComplete;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;

    UpdateEventTaskDto(UUID executorId, String title, String description, Boolean isComplete, LocalDateTime startDate, LocalDateTime finishDate) {
        this.executorId = executorId;

        if (title != null && (title.isBlank() || title.length() > 255)) {
            throw new EventTaskValidation("Название задачи не можеть быть меньше 1 символа и длинее 255 символов");
        }
        this.title = title;

        if (description != null && description.length() > 5000) {
            throw new EventTaskValidation("Описание задачи не может быть длинее 5000 символов");
        }
        this.description = description;

        this.isComplete = isComplete;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }
}
