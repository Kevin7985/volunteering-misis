package ru.misis.event;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.misis.category.model.Category;
import ru.misis.error.exceptions.Forbidden;
import ru.misis.event.dto.EventDto;
import ru.misis.event.dto.NewEventDto;
import ru.misis.event.dto.UpdateEventDto;
import ru.misis.event.exceptions.EventValidation;
import ru.misis.event.model.Event;
import ru.misis.event.model.EventState;
import ru.misis.event.model.QEvent;
import ru.misis.service.MapperService;
import ru.misis.service.ValidationService;
import ru.misis.skill.SkillRepository;
import ru.misis.skill.dto.SkillDto;
import ru.misis.skill.model.Skill;
import ru.misis.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final EntityManager entityManager;

    private final MapperService mapperService;
    private final ValidationService validationService;
    private final EventRepository eventRepository;
    private final SkillRepository skillRepository;

    @Override
    public EventDto createEvent(Authentication auth, NewEventDto eventDto) {
        if (eventDto.getFinishDate().isBefore(eventDto.getStartDate())) {
            throw new EventValidation("Дата окончания события не может быть раньше даты начала");
        }

        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Category category = validationService.validateCategory(eventDto.getCategoryId());

        List<Skill> skills = skillRepository.findAllById(eventDto.getSkills());

        Event event = mapperService.toEvent(eventDto, user, category, skills);

        log.info("Создано новое событие: " + event);
        return mapperService.toEventDto(eventRepository.save(event));
    }

    @Override
    public List<EventDto> searchEvents(Authentication auth, String title, List<UUID> creators, List<UUID> categories, List<UUID> skills, String location, LocalDateTime startDate, LocalDateTime finishDate, EventState state, Integer from, Integer size) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));

        QEvent qEvent = QEvent.event;

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<Event> query = queryFactory.selectFrom(qEvent);

        if (title != null) {
            query.where(qEvent.title.containsIgnoreCase(title));
        }

        if (creators != null && !creators.isEmpty()) {
            if (state == null || !state.equals(EventState.DRAFT)) {
                query.where(qEvent.creator.id.in(creators));
            } else {
                query.where(qEvent.creator.id.eq(user.getId()));
            }
        }

        if (categories != null && !categories.isEmpty()) {
            query.where(qEvent.category.id.in(categories));
        }

        if (location != null) {
            query.where(qEvent.location.containsIgnoreCase(location));
        }

        if (startDate != null) {
            query.where(qEvent.startDate.after(startDate.minusSeconds(1)));
        }

        if (finishDate != null) {
            query.where(qEvent.finishDate.before(finishDate.plusSeconds(1)));
        }

        if (state != null) {
            query.where(qEvent.state.eq(state));
        }

        log.info("Поиск событий");
        if (skills != null && !skills.isEmpty()) {
            return query.fetch().stream()
                    .filter(item -> {
                        List<UUID> eventSkills = item.getSkills().stream().map(Skill::getId).toList();

                        boolean isAny = false;

                        for (UUID skill : skills) {
                            if (eventSkills.contains(skill)) {
                                isAny = true;
                                break;
                            }
                        }

                        return isAny;
                    })
                    .skip(from)
                    .limit(size)
                    .map(mapperService::toEventDto)
                    .toList();
        }

        return query.offset(from).limit(size).fetch().stream()
                .map(mapperService::toEventDto)
                .toList();
    }

    @Override
    public EventDto getEventById(Authentication auth, UUID eventId) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);

        if (event.getState().equals(EventState.DRAFT)) {
            if (!event.getCreator().getId().equals(user.getId())) {
                throw new Forbidden();
            }
        }

        log.info("Получение события по id = " + eventId);
        return mapperService.toEventDto(event);
    }

    @Override
    public EventDto updateEventById(Authentication auth, UUID eventId, UpdateEventDto eventDto) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);

        if (!event.getCreator().getId().equals(user.getId())) {
            throw new Forbidden();
        }

        event.setTitle(eventDto.getTitle() == null ? event.getTitle() : eventDto.getTitle());
        event.setDescription(eventDto.getDescription() == null ? event.getDescription() : eventDto.getDescription());

        if (eventDto.getCategory() != null) {
            Category category = validationService.validateCategory(eventDto.getCategory().getId());
            event.setCategory(category);
        }

        if (eventDto.getSkills() != null) {
            List<UUID> skillIds = eventDto.getSkills().stream().map(SkillDto::getId).toList();
            List<Skill> skills = skillRepository.findAllById(skillIds);

            event.setSkills(skills);
        }

        event.setStartDate(eventDto.getStartDate() == null ? event.getStartDate() : eventDto.getStartDate());
        event.setFinishDate(eventDto.getFinishDate() == null ? event.getFinishDate() : eventDto.getFinishDate());

        if (event.getFinishDate().isBefore(event.getStartDate())) {
            throw new EventValidation("Дата окончания события не может быть раньше даты начала");
        }

        event.setParticipantLimit(eventDto.getParticipantLimit() == null ? event.getParticipantLimit() : eventDto.getParticipantLimit());
        event.setState(eventDto.getState() == null ? event.getState() : eventDto.getState());

        log.info("Обновлено событие: " + event);
        return mapperService.toEventDto(eventRepository.save(event));
    }

    @Override
    public void deleteEventById(Authentication auth, UUID eventId) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);

        if (!event.getCreator().getId().equals(user.getId())) {
            throw new Forbidden();
        }

        log.info("Удалено событие с id = " + eventId);
        eventRepository.deleteById(eventId);
    }
}
