package ru.practicum.explorewithme.main.request;

import ru.practicum.explorewithme.main.request.model.Request;

import java.util.List;

public interface RequestService {
    Request addNew(Long userId, Long eventId);

    List<Request> getAllRequestsByUser(Long userId);

    Request cancel(Long userId, Long requestId);

    List<Request> getRequests(Long userId, Long eventId);

    Request confirm(Long userId, Long eventId, Long reqId);

    Request reject(Long userId, Long eventId, Long reqId);
}
