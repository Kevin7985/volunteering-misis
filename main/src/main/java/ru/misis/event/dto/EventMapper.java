package ru.misis.event.dto;

import org.springframework.stereotype.Component;
import ru.misis.category.dto.CategoryDto;
import ru.misis.category.model.Category;
import ru.misis.event.model.Event;
import ru.misis.event.model.EventState;
import ru.misis.skill.dto.SkillDto;
import ru.misis.skill.model.Skill;
import ru.misis.user.dto.UserDto;
import ru.misis.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventMapper {
    public Event toEvent(NewEventDto eventDto, User user, Category category, List<Skill> skills) {
        return new Event(
                null,
                eventDto.getTitle(),
                eventDto.getDescription(),
                eventDto.getVolunteerFuncs(),
                user,
                category,
                skills,
                eventDto.getLocation(),
                eventDto.getPicture(),
                eventDto.getStartDate(),
                eventDto.getFinishDate(),
                eventDto.getParticipantLimit(),
                EventState.DRAFT,
                LocalDateTime.now()
        );
    }

    public EventDto toEventDto(Event event, UserDto userDto, CategoryDto categoryDto, List<SkillDto> skills) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getVolunteerFuncs(),
                userDto,
                categoryDto,
                skills,
                event.getLocation(),
                event.getPicture(),
                event.getStartDate(),
                event.getFinishDate(),
                event.getParticipantLimit(),
                event.getState(),
                event.getCreatedAt()
        );
    }
}
