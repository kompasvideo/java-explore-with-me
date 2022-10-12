package ru.practicum.explorewithme.main.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.event.model.AdminUpdateEventDto;
import ru.practicum.explorewithme.main.event.model.EventDto;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping("/admin/events")
    public List<EventDto> find(@RequestParam(required = false) List<Long> users,
                               @RequestParam(required = false) List<String> states,
                               @RequestParam(required = false) List<Long> categories,
                               @RequestParam(required = false) @FutureOrPresent LocalDateTime rangeStart,
                               @RequestParam(required = false) @Future LocalDateTime rangeEnd,
                               @RequestParam(required = false) String locationName,
                               @RequestParam(defaultValue = "0") Integer from,
                               @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение расширенного списка событий с помощью фильтра");
        return eventService.find(users, states, categories, rangeStart, rangeEnd, locationName, from, size);
    }

    @PutMapping("/admin/events/{eventId}")
    public EventDto change(@PathVariable Long eventId,
                           @RequestBody @Valid AdminUpdateEventDto eventDto) {
        log.info("Изменение события с eventId: {}", eventId);
        return eventService.change(eventId, eventDto);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventDto publish(@PathVariable Long eventId) {
        log.info("Опубликовать событие с eventId: {}", eventId);
        return eventService.publish(eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventDto reject(@PathVariable Long eventId) {
        log.info("Отклонить событие с eventId: {}", eventId);
        return eventService.reject(eventId);
    }
}
