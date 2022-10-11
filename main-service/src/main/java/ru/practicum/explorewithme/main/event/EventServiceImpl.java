package ru.practicum.explorewithme.main.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.explorewithme.main.category.model.Category;
import ru.practicum.explorewithme.main.category.CategoryRepository;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.exception.ValidationException;
import ru.practicum.explorewithme.main.event.model.*;
import ru.practicum.explorewithme.main.mappers.MapperEvent;
import ru.practicum.explorewithme.main.user.model.User;
import ru.practicum.explorewithme.main.user.UserRepository;
import ru.practicum.explorewithme.statmodule.StatModule;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatModule statModule;

    @Override
    @Transactional
    public EventDto addEvent(Long userId, EventParamDto eventParamDto) {

        if (eventParamDto.getAnnotation().length() < 10 || eventParamDto.getAnnotation().length() > 5000) {
            throw new ValidationException("Недопустимая длина слова");
        }

        if (eventParamDto.getDescription().length() < 10 || eventParamDto.getDescription().length() > 5000) {
            throw new ValidationException("Недопустимая длина слова");
        }

        if (eventParamDto.getTitle().length() < 3 || eventParamDto.getTitle().length() > 255) {
            throw new ValidationException("Недопустимая длина слова");
        }

        if (! userRepository.existsById(userId))
            throw new NotFoundException("EventServiceImpl addEvent, userId: " + userId);
        Long categoryId = eventParamDto.getCategory();
        if (! categoryRepository.existsById(categoryId))
            throw new NotFoundException("EventServiceImpl addEvent, categoryId: " + categoryId);

        Event event = eventRepository.save(MapperEvent.mapEventParamDtoToEvent(eventParamDto, userId));

        return mapEventToFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        if (! userRepository.existsById(userId))
            throw new NotFoundException("EventServiceImpl updateEvent, userId: " + userId);

        List<EventShortDto> eventShorts = eventRepository
                .findAllByInitiator(userId, PageRequest.of(from, size))
                .stream()
                .map(event -> mapEventToShortDto(event))
                .collect(Collectors.toList());

        return eventShorts;
    }

    @Override
    @Transactional
    public EventDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        if (! userRepository.existsById(userId))
            throw new NotFoundException("EventServiceImpl updateEvent, userId: " + userId);
        Long eventId = updateEventRequest.getEventId();
        if (! eventRepository.existsById(eventId))
            throw new NotFoundException("EventServiceImpl updateEvent, eventId: " + eventId);

        Event event = eventRepository.getReferenceById(updateEventRequest.getEventId());

        if (!event.getInitiator().equals(userId)) {
            throw new ValidationException("Событие может изменить только инициатор");
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Некорректное состояние");
        }

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new ValidationException("Менее чем за 2 часа до начала события");
        }

        if (updateEventRequest.getCategory() != null) {
            if (!categoryRepository.existsById(updateEventRequest.getCategory())) {
                throw new NotFoundException("Не найдена указанная категория");
            }
            event.setCategory(updateEventRequest.getCategory());
        }

        if (updateEventRequest.getAnnotation() != null) {
            if (updateEventRequest.getAnnotation().length() < 20
                    || updateEventRequest.getAnnotation().length() > 2000) {
                throw new ValidationException("Некорректная длина строки");
            }
            event.setAnnotation(updateEventRequest.getAnnotation());
        }

        if (updateEventRequest.getDescription() != null) {
            if (updateEventRequest.getDescription().length() < 20
                    || updateEventRequest.getDescription().length() > 7000) {
                throw new ValidationException("Некорректная длина строки");
            }
            event.setDescription(updateEventRequest.getDescription());
        }

        if (updateEventRequest.getTitle() != null) {
            if (updateEventRequest.getTitle().length() < 3
                    || updateEventRequest.getTitle().length() > 120) {
                throw new ValidationException("Некорректная длина строки");
            }
            event.setTitle(updateEventRequest.getTitle());
        }

        if (updateEventRequest.getEventDate() != null) {
            if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
                throw new ValidationException("Некорректное время");
            }
            event.setEventDate(updateEventRequest.getEventDate());
        }

        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }

        if (updateEventRequest.getParticipantLimit() != null) {
            if (updateEventRequest.getParticipantLimit() < 0) {
                throw new ValidationException("Некорректное число участников");
            }
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }

        return mapEventToFullDto(eventRepository.save(event));
    }

    @Override
    public EventDto getEvent(Long userId, Long eventId) {
        if (! userRepository.existsById(userId))
            throw new NotFoundException("EventServiceImpl getEvent, userId: " + userId);
        if (! eventRepository.existsById(eventId))
            throw new NotFoundException("EventServiceImpl getEvent, eventId: " + eventId);

        Event event = eventRepository.getReferenceById(eventId);

        if (!event.getInitiator().equals(userId)) {
            throw new ValidationException("Некорректный идентификатор пользователя");
        }

        return mapEventToFullDto(event);
    }

    @Override
    @Transactional
    public EventDto cancelEvent(Long userId, Long eventId) {
        if (! userRepository.existsById(userId))
            throw new NotFoundException("EventServiceImpl cancelEvent, userId: " + userId);
        if (! eventRepository.existsById(eventId))
            throw new NotFoundException("EventServiceImpl cancelEvent, eventId: " + eventId);

        Event event = eventRepository.getReferenceById(eventId);

        if (!event.getInitiator().equals(userId)) {
            throw new ValidationException("Пользователь не является инициатором события");
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Отменить можно только событие в состоянии ожидания");
        }

        event.setState(EventState.CANCELED);

        return mapEventToFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventShortDto> getAll(String text, List<Long> categories,
                                      Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable,
                                      String sort, Integer from, Integer size,
                                      HttpServletRequest request) {
        try {
            EventSort.valueOf(sort);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Несуществующий способ сортировки");
        }

        BooleanExpression onlyPublishedEvents = QEvent.event.state.eq(EventState.PUBLISHED);
        BooleanExpression basedExpression = onlyPublishedEvents;

        if (StringUtils.hasText(text)) {
            basedExpression = basedExpression
                    .and(QEvent.event.annotation.containsIgnoreCase(text)
                            .or(QEvent.event.description.containsIgnoreCase(text)));
        }

        if (categories != null && !categories.isEmpty()) {
            basedExpression = basedExpression
                    .and(QEvent.event.category
                            .in(categories));
        }

        if (paid != null) {
            basedExpression = basedExpression
                    .and(QEvent.event.paid
                            .eq(paid));
        }

        if (rangeStart != null) {
            basedExpression = basedExpression
                    .and(QEvent.event.eventDate
                            .after(rangeStart));
        } else {
            basedExpression = basedExpression
                    .and(QEvent.event.eventDate
                            .after(LocalDateTime.now()));
        }

        if (rangeEnd != null) {
            basedExpression = basedExpression
                    .and(QEvent.event.eventDate
                            .before(rangeEnd));
        }

        if (onlyAvailable) {
            basedExpression = basedExpression
                    .and(QEvent.event.confirmedRequests
                            .lt(QEvent.event.participantLimit));
        }

        String sortColumn;

        if (EventSort.EVENT_DATE.toString().equals(sort)) {
            sortColumn = "eventDate";
        } else {
            sortColumn = "views";
        }

        List<EventShortDto> events = eventRepository
                .findAll(basedExpression, PageRequest.of(from, size, Sort.by(sortColumn).descending()))
                .stream()
                .map(event -> mapEventToShortDto(event))
                .collect(Collectors.toList());

        if (events.isEmpty()) {
            throw new NotFoundException("Не найдено подходящих событий");
        }
        statModule.getAll(request);
        return events;
    }

    @Override
    public EventDto getEven(Long id, HttpServletRequest request) {
        if (! eventRepository.existsById(id))
            throw new NotFoundException("EventServiceImpl getEven, eventId: " + id);

        BooleanExpression onlyPublicEvents = QEvent.event.state.eq(EventState.PUBLISHED);

        Optional<Event> optionalEvent = eventRepository.findOne(onlyPublicEvents.and(QEvent.event.id.eq(id)));

        if (optionalEvent.isEmpty()) {
            throw new NotFoundException("Ничего не найдено по запросу");
        }

        Event event = optionalEvent.get();
        Long views = event.getViews();
        event.setViews(++views);

        EventDto fullDto = mapEventToFullDto(eventRepository.save(event));
        statModule.getAll(request);
        return fullDto;
    }

    @Override
    public List<EventDto> find(List<Long> users, List<String> states,
                               List<Long> categories, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Integer from, Integer size) {
        BooleanExpression basedExpression = QEvent.event.paid.eq(true).or(QEvent.event.paid.eq(false));

        if (rangeStart != null) {
            basedExpression = basedExpression.and(QEvent.event.eventDate
                    .after(rangeStart));
        }

        if (states != null && !states.isEmpty()) {
            List<EventState> stateList;

            try {
                stateList = states
                        .stream()
                        .map(state -> EventState.valueOf(state))
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Некорректное состояние");
            }

            basedExpression = basedExpression.and(QEvent.event.state.in(stateList));
        }

        if (users != null && !users.isEmpty()) {
            for (Long id : users) {
                if (!userRepository.existsById(id)) {
                    throw new NotFoundException("Пользователь не найден");
                }
            }

            basedExpression = basedExpression.and(QEvent.event.initiator.in(users));
        }

        if (categories != null && !categories.isEmpty()) {
            for (Long id : categories) {
                if (!categoryRepository.existsById(id)) {
                    throw new NotFoundException("Категория не найдена");
                }
            }

            basedExpression = basedExpression.and(QEvent.event.category.in(categories));
        }

        if (rangeEnd != null) {
            basedExpression = basedExpression.and(QEvent.event.eventDate.before(rangeEnd));
        }

        List<EventDto> events = eventRepository.findAll(basedExpression, PageRequest.of(from, size))
                .stream()
                .map(
                        event -> mapEventToFullDto(event)
                )
                .collect(Collectors.toList());

        if (events.isEmpty()) {
            throw new NotFoundException("События не найдены");
        }

        return events;
    }

    @Override
    @Transactional
    public EventDto change(Long eventId, AdminUpdateEventDto eventDto) {
        Event event = eventRepository.getReferenceById(eventId);

        if (StringUtils.hasText(eventDto.getAnnotation())) {
            event.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getCategory() != null) {
            event.setCategory(eventDto.getCategory());
        }

        if (StringUtils.hasText(eventDto.getDescription())) {
            event.setDescription(eventDto.getDescription());
        }

        if (eventDto.getEventDate() != null) {
            event.setEventDate(eventDto.getEventDate());
        }

        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }

        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }

        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(event.getTitle());
        }

        return mapEventToFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventDto publish(Long eventId) {
        if (! eventRepository.existsById(eventId))
            throw new NotFoundException("EventServiceImpl publish, eventId: " + eventId);

        Event event = eventRepository.getReferenceById(eventId);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1L))) {
            throw new ValidationException("Начинается менее чем через час от даты публикации");
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Событие находится не в состоянии ожидания публикации");
        }

        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());

        return mapEventToFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventDto reject(Long eventId) {
        if (! eventRepository.existsById(eventId))
            throw new NotFoundException("EventServiceImpl reject, eventId: " + eventId);
        Event event = eventRepository.getReferenceById(eventId);

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Событие находится не в состоянии ожидания публикации");
        }

        event.setState(EventState.CANCELED);
        return mapEventToFullDto(eventRepository.save(event));
    }

    private EventDto mapEventToFullDto(Event event) {
        Long id = event.getCategory();
        Category categoryDto = categoryRepository.findById(id).orElseThrow(() ->
            new NotFoundException("NotFoundException EventServiceImpl mapEventToFullDto, id: " + id));
        User userDto = userRepository.getReferenceById(event.getInitiator());
        return MapperEvent.mapEventToEventDto(event, categoryDto, userDto);
    }

    private EventShortDto mapEventToShortDto(Event event) {
        Long id = event.getCategory();
        Category category = categoryRepository.findById(id).orElseThrow(() ->
            new NotFoundException("NotFoundException EventServiceImpl mapEventToShortDto, id: " + id));
        User user = userRepository.getReferenceById(event.getInitiator());
        return MapperEvent.mapEventToShortDto(event, category, user);
    }
}
