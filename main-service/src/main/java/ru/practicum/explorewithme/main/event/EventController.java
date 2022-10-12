package ru.practicum.explorewithme.main.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.main.event.model.EventDto;
import ru.practicum.explorewithme.main.event.model.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getAll(@RequestParam(required = false) String text,
                                      @RequestParam(required = false) List<Long> categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) @FutureOrPresent LocalDateTime rangeStart,
                                      @RequestParam(required = false) @Future LocalDateTime rangeEnd,
                                      @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam String sort,
                                      @RequestParam(required = false) String locationName,
                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                      @RequestParam(defaultValue = "10") @Positive Integer size,
                                      HttpServletRequest request) {
        log.info("Получение короткого списка событий с помощью фильтра");
        return eventService.getAll(text, categories, paid, rangeStart,
            rangeEnd, onlyAvailable, sort, locationName, from, size, request);
    }

    @GetMapping("/events/{eventId}")
    public EventDto getEven(@PathVariable Long eventId,
                            HttpServletRequest request) {
        log.info("Найти событие с eventId: {}", eventId);
        return eventService.getEven(eventId, request);
    }
}
