package ru.misis.participant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.misis.participant.model.EventParticipant;
import ru.misis.participant.model.ParticipantRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {
    Page<EventParticipant> findAllByEventId(UUID eventId, Pageable pageable);

    List<EventParticipant> findAllByEventId(UUID eventId);

    Optional<EventParticipant> findByUserIdAndEventId(UUID userId, UUID eventId);

    Page<EventParticipant> findAllByUserIdAndRole(UUID userId, ParticipantRole role, Pageable pageable);
}
