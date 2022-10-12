package ru.practicum.explorewithme.main.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.event.model.EventDto;
import ru.practicum.explorewithme.main.event.model.EventParamDto;
import ru.practicum.explorewithme.main.event.model.EventShortDto;
import ru.practicum.explorewithme.main.event.model.UpdateEventRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventUsersController {
    private final EventService eventService;

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventDto getEvent(@PathVariable Long userId,
                             @PathVariable Long eventId) {
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventDto cancelEvent(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getEvents(userId, from, size);
    }

    @PatchMapping("/users/{userId}/events")
    public EventDto updateEvent(@PathVariable Long userId,
                                @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping("/users/{userId}/events")
    public EventDto addEvent(@PathVariable Long userId,
                             @RequestBody @Valid EventParamDto eventParamDto) {
        return eventService.addEvent(userId, eventParamDto);
    }
}
