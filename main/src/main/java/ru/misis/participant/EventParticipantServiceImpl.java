package ru.misis.participant;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.misis.error.exceptions.Forbidden;
import ru.misis.event.EventRepository;
import ru.misis.event.dto.EventDto;
import ru.misis.event.model.Event;
import ru.misis.event.model.EventRelation;
import ru.misis.event.model.EventState;
import ru.misis.participant.dto.EventParticipantDto;
import ru.misis.participant.dto.UpdateEventParticipantDto;
import ru.misis.participant.exceptions.EventParticipantNotFound;
import ru.misis.participant.exceptions.NoPlaceLeft;
import ru.misis.participant.model.EventParticipant;
import ru.misis.participant.model.ParticipantRole;
import ru.misis.service.MapperService;
import ru.misis.service.ValidationService;
import ru.misis.user.model.User;
import ru.misis.utils.Pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventParticipantServiceImpl implements EventParticipantService {
    private final MapperService mapperService;
    private final ValidationService validationService;
    private final EventParticipantRepository repository;
    private final EventRepository eventRepository;

    @Override
    public List<EventParticipantDto> getEventParticipants(Authentication auth, UUID eventId, Integer from, Integer size) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);

        if (event.getState().equals(EventState.DRAFT)) {
            if (!event.getCreator().getId().equals(user.getId())) {
                throw new Forbidden();
            }
        }

        List<EventParticipantDto> participants = new ArrayList<>();

        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Page<EventParticipant> page;
        Pagination pager = new Pagination(from, size);

        for (int i = pager.getPageStart(); i < pager.getPagesAmount(); i++) {
            pageable = PageRequest.of(i, pager.getPageSize(), sort);
            page = repository.findAllByEventId(eventId, pageable);
            participants.addAll(page.stream()
                    .map(mapperService::toEventParticipantDto)
                    .toList());
        }

        return participants;
    }

    @Override
    public EventParticipantDto participate(Authentication auth, UUID eventId, UUID userId) {
        User initiator = validationService.validateUser(UUID.fromString(auth.getName()));
        User user = validationService.validateUser(userId);
        Event event = validationService.validateEvent(eventId);

        if (initiator.getId().equals(user.getId())) {
            Optional<EventParticipant> participant = repository.findByUserIdAndEventId(user.getId(), eventId);
            if (participant.isPresent()) {
                return mapperService.toEventParticipantDto(participant.get());
            }
        } else {
            if (!event.getCreator().getId().equals(initiator.getId())) {
                Optional<EventParticipant> part = repository.findByUserIdAndEventId(initiator.getId(), eventId);
                if (part.isEmpty()) {
                    throw new Forbidden();
                }

                if (!part.get().getRole().equals(ParticipantRole.LEADER)) {
                    throw new Forbidden();
                }
            }
        }

        List<EventParticipant> participants = repository.findAllByEventId(eventId);

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= participants.size()) {
            throw new NoPlaceLeft("На данном мероприятии уже достигнуто максимальное количество участников");
        }

        EventParticipant newParticipant = new EventParticipant(
                null,
                user,
                event,
                ParticipantRole.PARTICIPANT
        );

        return mapperService.toEventParticipantDto(repository.save(newParticipant));
    }

    @Override
    public void cancelParticipation(Authentication auth, UUID eventId, UUID userId) {
        User initiator = validationService.validateUser(UUID.fromString(auth.getName()));
        User user = validationService.validateUser(userId);
        Event event = validationService.validateEvent(eventId);

        if (initiator.getId().equals(user.getId())) {
            Optional<EventParticipant> participant = repository.findByUserIdAndEventId(user.getId(), eventId);
            participant.ifPresent(eventParticipant -> repository.deleteById(eventParticipant.getId()));
            return;
        } else {
            if (!event.getCreator().getId().equals(initiator.getId())) {
                Optional<EventParticipant> part = repository.findByUserIdAndEventId(initiator.getId(), eventId);
                if (part.isEmpty()) {
                    throw new Forbidden();
                }

                if (!part.get().getRole().equals(ParticipantRole.LEADER)) {
                    throw new Forbidden();
                }
            }
        }

        Optional<EventParticipant> participant = repository.findByUserIdAndEventId(user.getId(), eventId);
        participant.ifPresent(eventParticipant -> repository.deleteById(eventParticipant.getId()));
    }

    @Override
    public EventParticipantDto updateParticipantRole(Authentication auth, UUID eventId, UUID userId, UpdateEventParticipantDto participant) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));
        Event event = validationService.validateEvent(eventId);

        if (!event.getCreator().getId().equals(user.getId())) {
            Optional<EventParticipant> part = repository.findByUserIdAndEventId(user.getId(), eventId);
            if (part.isEmpty()) {
                throw new Forbidden();
            }

            if (!part.get().getRole().equals(ParticipantRole.LEADER)) {
                throw new Forbidden();
            }
        }

        Optional<EventParticipant> participantToUpdate = repository.findByUserIdAndEventId(userId, eventId);
        if (participantToUpdate.isEmpty()) {
            throw new EventParticipantNotFound("Участник мероприятия с id = " + userId + " не найден");
        }

        EventParticipant part = participantToUpdate.get();

        part.setRole(participant.getRole());

        return mapperService.toEventParticipantDto(repository.save(part));
    }

    @Override
    public List<EventDto> getUserEvents(Authentication auth, UUID userId, EventRelation state, Integer from, Integer size) {
        User user = validationService.validateUser(UUID.fromString(auth.getName()));

        List<Event> events = new ArrayList<>();

        Pageable pageable;
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pagination pager = new Pagination(from, size);

        if (state.equals(EventRelation.CREATOR)) {
            Page<Event> page;

            for (int i = pager.getPageStart(); i < pager.getPagesAmount(); i++) {
                pageable = PageRequest.of(i, pager.getPageSize(), sort);
                page = eventRepository.findAllByCreatorId(userId, pageable);
                events.addAll(page.stream().toList());
            }
        } else {
            Page<EventParticipant> page;

            for (int i = pager.getPageStart(); i < pager.getPagesAmount(); i++) {
                pageable = PageRequest.of(i, pager.getPageSize(), sort);
                page = repository.findAllByUserIdAndRole(userId, ParticipantRole.valueOf(state.name()), pageable);
                events.addAll(page.stream()
                        .map(EventParticipant::getEvent)
                        .toList());
            }
        }

        return events.stream().map(mapperService::toEventDto).toList();
    }
}
