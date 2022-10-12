package ru.practicum.explorewithme.main.event;

import ru.practicum.explorewithme.main.event.model.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventDto addEvent(Long userId, EventParamDto eventParamDto);

    List<EventShortDto> getEvents(Long userId, Integer from, Integer size);

    EventDto updateEvent(Long userId, UpdateEventRequest updateEventRequest);

    EventDto getEvent(Long userId, Long eventId);

    EventDto cancelEvent(Long userId, Long eventId);

    List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

    EventDto getEven(Long id, HttpServletRequest request);

    List<EventDto> find(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventDto change(Long eventId, AdminUpdateEventDto eventDto);

    EventDto publish(Long eventId);

    EventDto reject(Long eventId);
}
