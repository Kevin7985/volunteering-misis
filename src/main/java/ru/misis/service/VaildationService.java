package ru.misis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.misis.skills.SkillRepository;
import ru.misis.skills.exceptions.SkillNotFound;
import ru.misis.skills.model.Skill;
import ru.misis.user.UserRepository;
import ru.misis.user.exceptions.UserNotFound;
import ru.misis.user.model.User;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaildationService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

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
}
