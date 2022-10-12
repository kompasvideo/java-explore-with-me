package ru.practicum.explorewithme.main.ficha.event;

import lombok.RequiredArgsConstructor;
import ru.practicum.explorewithme.main.category.model.Category;
import ru.practicum.explorewithme.main.event.model.*;
import ru.practicum.explorewithme.main.user.model.User;
import ru.practicum.explorewithme.main.user.model.UserDto;

import java.util.List;

@RequiredArgsConstructor
public class Mapper {

    public static EventDto mapEventToFullDto(Event event, Category categoryDto,
                                             User userDto, List<String> nearestLocations) {
        return EventDto.builder()
            .annotation(event.getAnnotation())
            .category(categoryDto)
            .confirmedRequests(event.getConfirmedRequests())
            .createdOn(event.getCreatedOn())
            .description(event.getDescription())
            .eventDate(event.getEventDate())
            .id(event.getId())
            .initiator(mapUserDtoToShort(userDto))
            .location(new Location(event.getLat(), event.getLon()))
            .paid(event.getPaid())
            .participantLimit(event.getParticipantLimit())
            .publishedOn(event.getPublishedOn())
            .requestModeration(event.getRequestModeration())
            .state(event.getState())
            .title(event.getTitle())
            .views(event.getViews())
            .nearestLocations(nearestLocations)
            .build();
    }

    public static UserDto mapUserDtoToShort(User userDto) {
        return UserDto.builder()
            .id(userDto.getId())
            .name(userDto.getName())
            .build();
    }

}
