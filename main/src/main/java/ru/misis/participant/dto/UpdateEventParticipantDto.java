package ru.misis.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.misis.participant.model.ParticipantRole;

@Data
@AllArgsConstructor
public class UpdateEventParticipantDto {
    private final ParticipantRole role;
}
