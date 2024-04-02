package ru.misis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.misis.category.CategoryRepository;
import ru.misis.category.exceptions.CategoryNotFound;
import ru.misis.category.model.Category;
import ru.misis.event.EventRepository;
import ru.misis.event.exceptions.EventNotFound;
import ru.misis.event.model.Event;
import ru.misis.participant.EventParticipantRepository;
import ru.misis.skill.SkillRepository;
import ru.misis.skill.exceptions.SkillNotFound;
import ru.misis.skill.model.Skill;
import ru.misis.user.UserRepository;
import ru.misis.user.exceptions.UserNotFound;
import ru.misis.user.model.User;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ValidationService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public User validateUser(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFound("Пользователь с id = " + id + " не найден")
        );
    }

    public Skill validateSkill(UUID id) {
        return skillRepository.findById(id).orElseThrow(
                () -> new SkillNotFound("Навык с id = " + id + " не найден")
        );
    }

    public Category validateCategory(UUID id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFound("Категория с id = " + id + " не найдена")
        );
    }

    public Event validateEvent(UUID id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new EventNotFound("Мероприятие с id = " + id + " не найдено")
        );
    }
}
