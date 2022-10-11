package ru.practicum.explorewithme.main.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.main.request.model.Request;
import ru.practicum.explorewithme.main.request.model.Status;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Boolean existsByRequesterAndEvent(Long userId, Long eventId);

    List<Request> findRequestByRequester(Long userId);

    List<Request> getRequestByEvent(Long eventId);

    List<Request> getRequestByEventAndStatus(Long eventId, Status status);
}
