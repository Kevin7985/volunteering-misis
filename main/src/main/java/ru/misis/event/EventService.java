package ru.misis.event;

import org.springframework.security.core.Authentication;
import ru.misis.event.dto.EventDto;
import ru.misis.event.dto.NewEventDto;
import ru.misis.event.dto.UpdateEventDto;
import ru.misis.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventService {
    public EventDto createEvent(Authentication auth, NewEventDto eventDto);

    public List<EventDto> searchEvents(
            Authentication auth,
            String title,
            List<UUID> creators,
            List<UUID> categories,
            List<UUID> skills,
            String location,
            LocalDateTime startDate,
            LocalDateTime finishDate,
            EventState state,
            Integer from,
            Integer size
    );

    public EventDto getEventById(Authentication auth, UUID eventId);

    public EventDto updateEventById(Authentication auth, UUID eventId, UpdateEventDto eventDto);

    public void deleteEventById(Authentication auth, UUID eventId);
}
