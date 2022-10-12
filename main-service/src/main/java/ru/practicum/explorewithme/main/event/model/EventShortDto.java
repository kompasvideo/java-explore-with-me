package ru.practicum.explorewithme.main.event.model;

import lombok.*;
import ru.practicum.explorewithme.main.category.model.Category;
import ru.practicum.explorewithme.main.user.model.UserDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class EventShortDto {
    private Long id;
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private UserDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
