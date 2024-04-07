package ru.misis.event;

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
import ru.misis.event.dto.EventDto;
import ru.misis.event.dto.NewEventDto;
import ru.misis.event.dto.UpdateEventDto;
import ru.misis.event.model.EventState;
import ru.misis.utils.models.ListResponse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@Validated
@RequiredArgsConstructor
@Tag(name = "Events", description = "Методы для работы с мероприятиями")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
public class EventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создание нового мероприятия")
    public EventDto createEvent(Authentication auth, @RequestBody @Valid NewEventDto eventDto) {
        return eventService.createEvent(auth, eventDto);
    }

    @GetMapping
    @Operation(summary = "Поиск по мероприятиям")
    public ListResponse<EventDto> findEvents(
            Authentication auth,
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "") List<UUID> creators,
            @RequestParam(required = false, defaultValue = "") List<UUID> categories,
            @RequestParam(required = false, defaultValue = "") List<UUID> skills,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime finishDate,
            @RequestParam(required = false) EventState state,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "20") @Min(1) Integer size
            ) {

        return eventService.searchEvents(
                auth,
                title,
                creators,
                categories,
                skills,
                location,
                startDate,
                finishDate,
                state,
                from,
                size
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение мероприятия по идентификатору")
    public EventDto getEventById(Authentication auth, @PathVariable UUID id) {
        return eventService.getEventById(auth, id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление мероприятия по идентификатору")
    public EventDto updateEventById(Authentication auth, @PathVariable UUID id, @RequestBody UpdateEventDto eventDto) {
        return eventService.updateEventById(auth, id, eventDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удаление мероприятия по идентификатору")
    public void deleteEventById(Authentication auth, @PathVariable UUID id) {
        eventService.deleteEventById(auth, id);
    }
}
