package ru.practicum.explorewithme.main.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.request.model.Request;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<Request> getRequests(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("Найти все запросы c userId: {} и eventId: {}", userId, eventId);
        return requestService.getRequests(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{requestId}/confirm")
    public Request confirm(@PathVariable Long userId,
                           @PathVariable Long eventId,
                           @PathVariable Long requestId) {
        log.info("Перевод статуса на 'Подтвержден', requestId: {}, eventId: {}, userId: {} ",
            requestId, eventId, userId);
        return requestService.confirm(userId, eventId, requestId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{requestId}/reject")
    public Request reject(@PathVariable Long userId,
                          @PathVariable Long eventId,
                          @PathVariable Long requestId) {
        log.info("Перевод статуса на 'Отклонен', requestId: {}, eventId: {}, userId: {} ",
            requestId,  eventId, userId);
        return requestService.reject(userId, eventId, requestId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<Request> getAllRequestsByUser(@PathVariable Long userId) {
        log.info("Найти все запросы с userId: {}", userId);
        return requestService.getAllRequestsByUser(userId);
    }

    @PostMapping("/users/{userId}/requests")
    public Request addNew(@PathVariable Long userId,
                          @RequestParam Long eventId) {
        log.info("Добавить запрос с userId: {}, eventId: {}", userId, eventId);
        return requestService.addNew(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public Request cancel(@PathVariable Long userId,
                                 @PathVariable Long requestId) {
        log.info("Закрыть запрос, requestId: {}, userId: {}", requestId, userId);
        return requestService.cancel(userId, requestId);
    }

}
