package ru.misis.participant;

import org.springframework.security.core.Authentication;
import ru.misis.event.dto.EventDto;
import ru.misis.event.model.EventRelation;
import ru.misis.participant.dto.EventParticipantDto;
import ru.misis.participant.dto.UpdateEventParticipantDto;
import ru.misis.utils.models.ListResponse;

import java.util.List;
import java.util.UUID;

public interface EventParticipantService {
    ListResponse<EventParticipantDto> getEventParticipants(Authentication auth, UUID eventId, Integer from, Integer size);

    EventParticipantDto participate(Authentication auth, UUID eventId, UUID userId);

    void cancelParticipation(Authentication auth, UUID eventId, UUID userId);

    EventParticipantDto updateParticipantRole(Authentication auth, UUID eventId, UUID userId, UpdateEventParticipantDto participant);

    List<EventDto> getUserEvents(Authentication auth, UUID userId, EventRelation state, Integer from, Integer size);

}
