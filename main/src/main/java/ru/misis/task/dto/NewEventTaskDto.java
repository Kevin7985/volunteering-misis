package ru.misis.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewEventTaskDto {
    @NotBlank
    @Size(min = 1, max = 255)
    private final String title;

    @Size(max = 5000)
    private final String description;

    private Boolean isComplete;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
}
