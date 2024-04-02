package ru.misis.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.misis.error.exceptions.Forbidden;
import ru.misis.event.model.Event;
import ru.misis.event.model.EventState;
import ru.misis.participant.EventParticipantRepository;
import ru.misis.participant.model.EventParticipant;
import ru.misis.participant.model.ParticipantRole;
import ru.misis.service.MapperService;
import ru.misis.service.ValidationService;
import ru.misis.task.dto.EventTaskDto;
import ru.misis.task.dto.NewEventTaskDto;
import ru.misis.task.dto.UpdateEventTaskDto;
import ru.misis.task.exceptions.EventTaskValidation;
import ru.misis.task.model.EventTask;
import ru.misis.user.model.User;
import ru.misis.utils.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventTaskServiceImpl implements EventTaskService {
    private final MapperService mapperService;
    private final ValidationService validationService;
    private final EventTaskRepository repository;
    private final EventParticipantRepository participantRepository;

    @Override
    public EventTaskDto createEventTask(Authentication auth, UUID eventId, NewEventTaskDto eventTaskDto) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);

        validateOperation(user, event, "Невозможно создать задачу в завершённом мероприятии");

        EventTask task = mapperService.toEventTask(eventTaskDto, event, user);

        if (task.getStartDate().isAfter(task.getFinishDate())) {
            throw new EventTaskValidation("Дата окончания выполнения задачи не может быть раньше даты начала");
        }

        log.info("Создана новая задача к мероприятию: " + task);
        return mapperService.toEventTaskDto(repository.save(task));
    }

    @Override
    public List<EventTaskDto> searchTasks(Authentication auth, UUID eventId, Integer from, Integer size) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);

        if (!user.getId().equals(event.getCreator().getId())) {
            Optional<EventParticipant> participant = participantRepository.findByUserIdAndEventId(user.getId(), event.getId());
            if (participant.isEmpty()) {
                throw new Forbidden();
            }
        }

        List<EventTaskDto> eventTasks = new ArrayList<>();

        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Page<EventTask> page;
        Pagination pager = new Pagination(from, size);

        for (int i = pager.getPageStart(); i < pager.getPagesAmount(); i++) {
            pageable = PageRequest.of(i, pager.getPageSize(), sort);
            page = repository.findByEventId(eventId, pageable);
            eventTasks.addAll(page.stream()
                    .map(mapperService::toEventTaskDto)
                    .toList());
        }

        log.info("Получение списка задач мероприятия");
        return eventTasks;
    }

    @Override
    public EventTaskDto getEventTaskById(Authentication auth, UUID eventId, UUID taskId) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);
        EventTask eventTask = validationService.validateTaskByIdAndEventId(taskId, eventId);

        if (!user.getId().equals(event.getCreator().getId())) {
            Optional<EventParticipant> participant = participantRepository.findByUserIdAndEventId(user.getId(), event.getId());
            if (participant.isEmpty()) {
                throw new Forbidden();
            }
        }

        log.info("Получена задача по id = " + taskId);
        return mapperService.toEventTaskDto(eventTask);
    }

    @Override
    public EventTaskDto updateEventTaskById(Authentication auth, UUID eventId, UUID taskId, UpdateEventTaskDto eventTaskDto) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);
        EventTask eventTask = validationService.validateTaskByIdAndEventId(taskId, eventId);

        if (event.getState().equals(EventState.FINISHED)) {
            throw new EventTaskValidation("Задача не может быть обновлена после завершения мероприятия");
        }

        if (user.getId().equals(event.getCreator().getId())) {
            updateEventTaskAdmin(eventTask, eventTaskDto);
        } else {
            Optional<EventParticipant> participant = participantRepository.findByUserIdAndEventId(user.getId(), event.getId());
            if (participant.isEmpty()) {
                throw new Forbidden();
            }

            if (participant.get().getRole().equals(ParticipantRole.LEADER)) {
                updateEventTaskAdmin(eventTask, eventTaskDto);
            } else if (eventTask.getExecutor() != null && user.getId().equals(eventTask.getExecutor().getId())) {
                eventTask.setIsComplete(eventTaskDto.getIsComplete() == null ? eventTask.getIsComplete() : eventTaskDto.getIsComplete());
            } else {
                throw new Forbidden();
            }
        }

        if (eventTask.getStartDate().isAfter(eventTask.getFinishDate())) {
            throw new EventTaskValidation("Дата окончания выполнения задачи не может быть раньше даты начала");
        }

        return mapperService.toEventTaskDto(repository.save(eventTask));
    }

    @Override
    public void deleteEventTask(Authentication auth, UUID eventId, UUID taskId) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);
        EventTask eventTask = validationService.validateTaskByIdAndEventId(taskId, eventId);

        validateOperation(user, event, "Невозможно удалить задачу из завершённого мероприятия");

        log.info("Удалена задача из мероприятия: " + eventTask);
        repository.deleteById(eventTask.getId());
    }

    private void validateOperation(User user, Event event, String msg) {
        if (event.getState().equals(EventState.FINISHED)) {
            throw new EventTaskValidation(msg);
        }

        if (!user.getId().equals(event.getCreator().getId())) {
            Optional<EventParticipant> participant = participantRepository.findByUserIdAndEventId(user.getId(), event.getId());
            if (participant.isEmpty()) {
                throw new Forbidden();
            }

            if (!participant.get().getRole().equals(ParticipantRole.LEADER)) {
                throw new Forbidden();
            }
        }
    }

    private void updateEventTaskAdmin(EventTask eventTask, UpdateEventTaskDto taskDto) {
        if (taskDto.getExecutorId() != null) {
            User user = validationService.validateUser(taskDto.getExecutorId());
            eventTask.setExecutor(user);
        }

        eventTask.setTitle(taskDto.getTitle() == null ? eventTask.getTitle() : taskDto.getTitle());
        eventTask.setDescription(taskDto.getDescription() == null ? eventTask.getDescription() : taskDto.getDescription());
        eventTask.setIsComplete(taskDto.getIsComplete() == null ? eventTask.getIsComplete() : taskDto.getIsComplete());
        eventTask.setStartDate(taskDto.getStartDate() == null ? eventTask.getStartDate() : taskDto.getStartDate());
        eventTask.setFinishDate(taskDto.getFinishDate() == null ? eventTask.getFinishDate() : taskDto.getFinishDate());
    }
}
