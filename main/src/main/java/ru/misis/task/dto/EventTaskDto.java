package ru.misis.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.misis.event.dto.EventDto;
import ru.misis.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EventTaskDto {
    private final UUID id;
    private final EventDto event;
    private final UserDto creator;
    private final UserDto executor;
    private final String title;
    private final String description;
    private final Boolean isComplete;
    private final LocalDateTime startDate;
    private final LocalDateTime finishDate;
    private final LocalDateTime createdAt;
}
