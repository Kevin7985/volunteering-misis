package ru.misis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.misis.category.dto.CategoryDto;
import ru.misis.category.dto.CategoryMapper;
import ru.misis.category.dto.NewCategoryDto;
import ru.misis.category.model.Category;
import ru.misis.event.dto.EventDto;
import ru.misis.event.dto.EventMapper;
import ru.misis.event.dto.NewEventDto;
import ru.misis.event.model.Event;
import ru.misis.participant.dto.EventParticipantDto;
import ru.misis.participant.dto.EventParticipantMapper;
import ru.misis.participant.model.EventParticipant;
import ru.misis.skill.dto.NewSkillDto;
import ru.misis.skill.dto.SkillDto;
import ru.misis.skill.dto.SkillMapper;
import ru.misis.skill.model.Skill;
import ru.misis.task.dto.EventTaskDto;
import ru.misis.task.dto.EventTaskMapper;
import ru.misis.task.dto.NewEventTaskDto;
import ru.misis.task.model.EventTask;
import ru.misis.user.dto.NewUserDto;
import ru.misis.user.dto.UserDto;
import ru.misis.user.dto.UserMapper;
import ru.misis.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapperService {
    private final UserMapper userMapper;
    private final SkillMapper skillMapper;
    private final CategoryMapper categoryMapper;
    private final EventMapper eventMapper;
    private final EventParticipantMapper eventParticipantMapper;
    private final EventTaskMapper eventTaskMapper;

    public User toUser(NewUserDto userDto) {
        if (userDto == null) {
            return null;
        }

        return userMapper.toUser(userDto);
    }

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        List<SkillDto> skillDtoList = user.getSkills().stream().map(this::toSkillDto).toList();
        return userMapper.toUserDto(user, skillDtoList);
    }

    public Skill toSkill(NewSkillDto skillDto) {
        if (skillDto == null) {
            return null;
        }

        return skillMapper.toSkill(skillDto);
    }

    public Skill toSkill(SkillDto skillDto) {
        if (skillDto == null) {
            return null;
        }

        return skillMapper.toSkill(skillDto);
    }

    public SkillDto toSkillDto(Skill skill) {
        if (skill == null) {
            return null;
        }

        return skillMapper.toSkillDto(skill);
    }

    public Category toCategory(NewCategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }

        return categoryMapper.toCategory(categoryDto);
    }

    public CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }

        return categoryMapper.toCategoryDto(category);
    }

    public Event toEvent(NewEventDto eventDto, User user, Category category, List<Skill> skills) {
        if (eventDto == null) {
            return null;
        }

        return eventMapper.toEvent(eventDto, user, category, skills);
    }

    public EventDto toEventDto(Event event) {
        if (event == null) {
            return null;
        }

        return eventMapper.toEventDto(
                event,
                toUserDto(event.getCreator()),
                toCategoryDto(event.getCategory()),
                event.getSkills().stream().map(this::toSkillDto).toList()
        );
    }

    public EventParticipantDto toEventParticipantDto(EventParticipant participant) {
        if (participant == null) {
            return null;
        }

        return eventParticipantMapper.toEventParticipantDto(
                participant,
                toUserDto(participant.getUser()),
                toEventDto(participant.getEvent())
        );
    }

    public EventTask toEventTask(NewEventTaskDto taskDto, Event event, User creator) {
        if (taskDto == null) {
            return null;
        }

        return eventTaskMapper.toEventTask(taskDto, event, creator);
    }

    public EventTaskDto toEventTaskDto(EventTask task) {
        if (task == null) {
            return null;
        }

        return eventTaskMapper.toEventTaskDto(
                task,
                toEventDto(task.getEvent()),
                toUserDto(task.getCreator()),
                toUserDto(task.getExecutor())
        );
    }
}
