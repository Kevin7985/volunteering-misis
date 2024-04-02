package ru.misis.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.misis.event.model.Event;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    Page<Event> findAllByCreatorId(UUID creatorId, Pageable pageable);
}
