package ru.practicum.explorewithme.main.event.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AdminUpdateEventDto {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Boolean paid;
    private Long participantLimit = 0L;
    private Boolean requestModeration;
    private String title;
}
