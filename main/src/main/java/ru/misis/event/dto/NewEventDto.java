package ru.misis.event.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 1, max = 255)
    private final String title;

    @Size(max = 5000)
    private final String description;

    @Size(max = 5000)
    private final String volunteerFuncs;

    private final UUID categoryId;
    private final List<UUID> skills;

    private final String location;

    private final String picture;

    @FutureOrPresent
    private LocalDateTime startDate;

    @FutureOrPresent
    private LocalDateTime finishDate;

    @PositiveOrZero
    private Integer participantLimit;
}
