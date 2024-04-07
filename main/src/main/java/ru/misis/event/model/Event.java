package ru.misis.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.misis.category.model.Category;
import ru.misis.skill.model.Skill;
import ru.misis.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(name = "event_skills",
    joinColumns = { @JoinColumn(name = "event_id") },
    inverseJoinColumns = { @JoinColumn(name = "skill_id") })
    private List<Skill> skills;

    private String location;
    private String picture;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private Integer participantLimit;

    @Enumerated(EnumType.STRING)
    private EventState state;

    private LocalDateTime createdAt;
}
