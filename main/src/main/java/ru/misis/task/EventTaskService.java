package ru.misis.task;

import org.springframework.security.core.Authentication;
import ru.misis.task.dto.EventTaskDto;
import ru.misis.task.dto.NewEventTaskDto;
import ru.misis.task.dto.UpdateEventTaskDto;

import java.util.List;
import java.util.UUID;

public interface EventTaskService {
    EventTaskDto createEventTask(Authentication auth, UUID eventId, NewEventTaskDto eventTaskDto);

    List<EventTaskDto> searchTasks(Authentication auth, UUID eventId, Integer from, Integer size);

    EventTaskDto getEventTaskById(Authentication auth, UUID eventId, UUID taskId);

    EventTaskDto updateEventTaskById(Authentication auth, UUID eventId, UUID taskId, UpdateEventTaskDto eventTaskDto);

    void deleteEventTask(Authentication auth, UUID eventId, UUID taskId);
}
