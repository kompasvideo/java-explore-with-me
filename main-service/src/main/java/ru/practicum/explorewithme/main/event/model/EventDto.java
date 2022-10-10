package ru.practicum.explorewithme.main.event.model;

import lombok.*;
import ru.practicum.explorewithme.main.category.model.Category;
import ru.practicum.explorewithme.main.user.model.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class EventDto {
    private String annotation;
    private Long id;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Category category;
    private UserDto initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
    private List<String> nearestLocations;
}
