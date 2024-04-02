package ru.misis.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.misis.event.dto.EventDto;
import ru.misis.participant.model.ParticipantRole;
import ru.misis.user.dto.UserDto;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EventParticipantDto {
    private final UserDto user;
    private final EventDto event;
    private final ParticipantRole role;
}
