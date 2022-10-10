package ru.practicum.explorewithme.main.event.model;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatData {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
