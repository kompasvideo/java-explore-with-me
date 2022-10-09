package ru.practicum.explorewithme.main.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.event.model.Event;
import ru.practicum.explorewithme.main.event.model.EventState;
import ru.practicum.explorewithme.main.event.EventRepository;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.exception.ValidationException;
import ru.practicum.explorewithme.main.request.model.Request;
import ru.practicum.explorewithme.main.request.model.Status;
import ru.practicum.explorewithme.main.user.UserRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Request addNew(Long userId, Long eventId) {

        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId.toString()));

        if (requestRepository.existsByEventIdAndUserId(userId, eventId)) {
            throw new ValidationException("Запрос уже был отправлен");
        }

        Event event = eventRepository.getReferenceById(eventId);

        if (event.getInitiator().equals(userId)) {
            throw new ValidationException("Инициатор не может добавлять запрос");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Нельзя учавствовать в неопубликованном событии");
        }

        if (event.getParticipantLimit() > 0) {
            if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
                throw new ValidationException("Достигнут лимит по количеству запросов");
            }
        }

        Status status;

        if (!event.getRequestModeration()) {
            Long requests = event.getConfirmedRequests();
            event.setConfirmedRequests(++requests);

            eventRepository.save(event);

            status = Status.CONFIRMED;
        } else {
            status = Status.PENDING;
        }

        return requestRepository.save(Request.builder()
                .requester(userId)
                .event(eventId)
                .created(LocalDateTime.now())
                .status(status)
                .build());
    }

    @Override
    public List<Request> getAllRequestsByUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId.toString()));
        List<Request> requestDtoList = requestRepository.findRequestByRequester(userId);

        if (requestDtoList.isEmpty()) {
            throw new NotFoundException("Запросы не найдены");
        }

        return requestDtoList;
    }

    @Override
    @Transactional
    public Request cancel(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId.toString()));
        requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(requestId.toString()));

        Request requestDto = requestRepository.getReferenceById(requestId);

        if (!requestDto.getRequester().equals(userId)) {
            throw new ValidationException("Неверный id пользователя");
        }

        if (requestDto.getStatus().equals(Status.CANCELED)) {
            throw new ValidationException("Повторное удаление события");
        }

        if (requestDto.getStatus().equals(Status.CONFIRMED)) {
            Event event = eventRepository.getReferenceById(requestDto.getEvent());

            Long requests = event.getConfirmedRequests();
            event.setConfirmedRequests(--requests);

            eventRepository.save(event);
        }

        requestDto.setStatus(Status.CANCELED);
        return requestRepository.save(requestDto);
    }

    @Override
    public List<Request> getRequests(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId.toString()));

        Event event = eventRepository.getReferenceById(eventId);

        if (!event.getInitiator().equals(userId)) {
            throw new ValidationException("Пользователь не является инициатором события");
        }

        List<Request> requestDtoList = requestRepository.getRequestByEvent(eventId);

        if (requestDtoList.isEmpty()) {
            throw new NotFoundException("Не найдены запросы");
        }

        return requestDtoList;
    }

    @Override
    @Transactional
    public Request confirm(Long userId, Long eventId, Long reqId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId.toString()));
        requestRepository.findById(reqId).orElseThrow(() -> new NotFoundException(reqId.toString()));

        Event event = eventRepository.getReferenceById(eventId);

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return requestRepository.getReferenceById(reqId);
        }

        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ValidationException("Достигнут лимит участников");
        }

        Request requestDto = requestRepository.getReferenceById(reqId);

        if (!requestDto.getStatus().equals(Status.PENDING)) {
            throw new ValidationException("Некорректный статус заявки " + requestDto.getStatus());
        }

        requestDto.setStatus(Status.CONFIRMED);
        requestRepository.save(requestDto);

        Long requests = event.getConfirmedRequests();
        event.setConfirmedRequests(++requests);
        eventRepository.save(event);

        if (requests.equals(event.getParticipantLimit())) {
            List<Request> requestDtoList = requestRepository
                    .getRequestByEventAndStatus(eventId, Status.PENDING);

            for (Request request : requestDtoList) {
                request.setStatus(Status.REJECTED);
                requestRepository.save(request);
            }
        }

        return Request.builder()
                .id(requestDto.getId())
                .created(requestDto.getCreated())
                .requester(requestDto.getRequester())
                .event(requestDto.getEvent())
                .status(requestDto.getStatus())
                .build();
    }

    @Override
    @Transactional
    public Request reject(Long userId, Long eventId, Long reqId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId.toString()));
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(eventId.toString()));
        requestRepository.findById(reqId).orElseThrow(() -> new NotFoundException(reqId.toString()));

        Request requestDto = requestRepository.getReferenceById(reqId);

        if (!requestDto.getStatus().equals(Status.PENDING)) {
            throw new ValidationException("Некорректный статус");
        }

        requestDto.setStatus(Status.REJECTED);
        return requestRepository.save(requestDto);
    }
}
