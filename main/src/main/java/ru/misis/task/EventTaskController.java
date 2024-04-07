package ru.misis.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.misis.task.dto.EventTaskDto;
import ru.misis.task.dto.NewEventTaskDto;
import ru.misis.task.dto.UpdateEventTaskDto;
import ru.misis.utils.models.ListResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Event tasks", description = "Методы для работы с задачами к мероприятию")
@CrossOrigin(origins = "*")
public class EventTaskController {
    private final EventTaskService eventTaskService;

    @PostMapping("/{eventId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание новой задачи в мероприятии")
    public EventTaskDto createEventTask(Authentication auth, @PathVariable UUID eventId, @RequestBody NewEventTaskDto eventTaskDto) {
        return eventTaskService.createEventTask(auth, eventId, eventTaskDto);
    }

    @GetMapping("/{eventId}/tasks")
    @Operation(summary = "Получение задач мероприятия")
    public ListResponse<EventTaskDto> searchTasksInEvent(
            Authentication auth,
            @PathVariable UUID eventId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return eventTaskService.searchTasks(auth, eventId, from, size);
    }

    @GetMapping("/{eventId}/tasks/{taskId}")
    @Operation(summary = "Получение задачи по идентификатору")
    public EventTaskDto getTaskByEventIdAndId(Authentication auth, @PathVariable UUID eventId, @PathVariable UUID taskId) {
        return eventTaskService.getEventTaskById(auth, eventId, taskId);
    }

    @PatchMapping("/{eventId}/tasks/{taskId}")
    @Operation(summary = "Обновление задачи по идентификатору")
    public EventTaskDto updateTaskByEventIdAndId(Authentication auth, @PathVariable UUID eventId, @PathVariable UUID taskId, @RequestBody UpdateEventTaskDto taskDto) {
        return eventTaskService.updateEventTaskById(auth, eventId, taskId, taskDto);
    }

    @DeleteMapping("/{eventId}/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление задачи в мероприятии")
    public void deleteEventTask(Authentication auth, @PathVariable UUID eventId, @PathVariable UUID taskId) {
        eventTaskService.deleteEventTask(auth, eventId, taskId);
    }
}
