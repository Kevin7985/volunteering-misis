package ru.misis.participant;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.misis.event.dto.EventDto;
import ru.misis.event.model.EventRelation;
import ru.misis.participant.dto.EventParticipantDto;
import ru.misis.participant.dto.UpdateEventParticipantDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*")
public class EventParticipantController {
    private final EventParticipantService service;

    @GetMapping("/events/{eventId}/participants")
    @Tag(name = "Event participants", description = "Методы для работы с участниками мероприятий")
    @Operation(summary = "Получение участников по id мероприятия")
    public List<EventParticipantDto> getEventParticipants(
            Authentication auth,
            @PathVariable UUID eventId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return service.getEventParticipants(auth, eventId, from, size);
    }

    @PostMapping("/events/{eventId}/participate/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Tag(name = "Event participants", description = "Методы для работы с участниками мероприятий")
    @Operation(summary = "Отправка заявки на участие в мероприятии")
    public EventParticipantDto eventParticipateAdd(Authentication auth, @PathVariable UUID eventId, @PathVariable UUID userId) {
        return service.participate(auth, eventId, userId);
    }

    @DeleteMapping("/events/{eventId}/participate/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Tag(name = "Event participants", description = "Методы для работы с участниками мероприятий")
    @Operation(summary = "Отмена заявки на участие в мероприятии")
    public void eventParticipateDelete(Authentication auth, @PathVariable UUID eventId, @PathVariable UUID userId) {
        service.cancelParticipation(auth, eventId, userId);
    }

    @PatchMapping("/events/{eventId}/participants/{userId}")
    @Tag(name = "Event participants", description = "Методы для работы с участниками мероприятий")
    @Operation(summary = "Обновление информации об участнике мероприятии")
    public EventParticipantDto updateEventParticipant(Authentication auth, @PathVariable UUID eventId, @PathVariable UUID userId, UpdateEventParticipantDto participant) {
        return service.updateParticipantRole(auth, eventId, userId, participant);
    }

    @GetMapping("/users/{userId}/events")
    @Tag(name = "Users", description = "Методы для работы с пользователями")
    @Operation(summary = "Получение событий пользователя")
    public List<EventDto> getUserEvents(
            Authentication auth,
            @PathVariable UUID userId,
            @RequestParam(required = true) EventRelation state,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "20") @Min(1) Integer size) {
        return service.getUserEvents(auth, userId, state, from, size);
    }
}
