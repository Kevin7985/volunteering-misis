package ru.misis.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.misis.event.model.Event;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
