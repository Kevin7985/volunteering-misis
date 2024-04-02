package ru.misis.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.misis.task.model.EventTask;

import java.util.Optional;
import java.util.UUID;

public interface EventTaskRepository extends JpaRepository<EventTask, UUID> {
    Optional<EventTask> findByIdAndEventId(UUID id, UUID eventId);

    Page<EventTask> findByEventId(UUID eventId, Pageable pageable);
}
