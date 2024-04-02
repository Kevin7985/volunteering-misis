package ru.misis.participant.dto;

import org.springframework.stereotype.Component;
import ru.misis.event.dto.EventDto;
import ru.misis.participant.model.EventParticipant;
import ru.misis.user.dto.UserDto;

@Component
public class EventParticipantMapper {
    public EventParticipantDto toEventParticipantDto(EventParticipant participant, UserDto user, EventDto event) {
        return new EventParticipantDto(
                user,
                event,
                participant.getRole()
        );
    }
}
