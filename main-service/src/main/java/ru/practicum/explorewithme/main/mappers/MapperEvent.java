package ru.practicum.explorewithme.main.mappers;

import lombok.RequiredArgsConstructor;
import ru.practicum.explorewithme.main.category.model.Category;
import ru.practicum.explorewithme.main.event.model.*;
import ru.practicum.explorewithme.main.user.model.User;
import java.time.LocalDateTime;
import static ru.practicum.explorewithme.main.mappers.MapperUser.mapUserToUserDto;

@RequiredArgsConstructor
public class MapperEvent {
    public static Event mapEventParamDtoToEvent(EventParamDto eventParamDto, Long userId) {
        return Event.builder()
                .annotation(eventParamDto.getAnnotation())
                .category(eventParamDto.getCategory())
                .description(eventParamDto.getDescription())
                .eventDate(eventParamDto.getEventDate())
                .lon(eventParamDto.getLocation().getLon())
                .lat(eventParamDto.getLocation().getLat())
                .paid(eventParamDto.getPaid())
                .participantLimit(eventParamDto.getParticipantLimit())
                .requestModeration(eventParamDto.getRequestModeration())
                .title(eventParamDto.getTitle())
                .state(EventState.PENDING)
                .initiator(userId)
                .views(0L)
                .createdOn(LocalDateTime.now())
                .confirmedRequests(0L)
                .build();
    }

    public static EventDto mapEventToEventDto(Event event, Category categoryDto,
                                              User userDto) {
        return EventDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(mapUserToUserDto(userDto))
                .location(new Location(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto mapEventToShortDto(Event event, Category categoryDto, User userDto) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(mapUserToUserDto(userDto))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
