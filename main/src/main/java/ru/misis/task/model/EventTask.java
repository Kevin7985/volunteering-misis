package ru.misis.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.misis.event.model.Event;
import ru.misis.user.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_tasks")
public class EventTask {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;

    private String title;
    private String description;
    private Boolean isComplete;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private LocalDateTime createdAt;
}
