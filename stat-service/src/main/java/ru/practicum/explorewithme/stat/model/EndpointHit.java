package ru.practicum.explorewithme.stat.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "stat_events")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String app;
    @NotNull
    private String uri;
    @NotNull
    private String ip;
    @NotNull
    private LocalDateTime timestamp;
}
