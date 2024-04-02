package ru.misis.task.dto;

import org.springframework.stereotype.Component;
import ru.misis.event.dto.EventDto;
import ru.misis.event.model.Event;
import ru.misis.task.model.EventTask;
import ru.misis.user.dto.UserDto;
import ru.misis.user.model.User;

import java.time.LocalDateTime;

@Component
public class EventTaskMapper {
    public EventTask toEventTask(NewEventTaskDto taskDto, Event event, User creator) {
        return new EventTask(
                null,
                event,
                creator,
                null,
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getIsComplete(),
                taskDto.getStartDate(),
                taskDto.getFinishDate(),
                LocalDateTime.now()
        );
    }

    public EventTaskDto toEventTaskDto(EventTask task, EventDto eventDto, UserDto creatorDto, UserDto executorDto) {
        return new EventTaskDto(
                task.getId(),
                eventDto,
                creatorDto,
                executorDto,
                task.getTitle(),
                task.getDescription(),
                task.getIsComplete(),
                task.getStartDate(),
                task.getFinishDate(),
                task.getCreatedAt()
        );
    }
}
