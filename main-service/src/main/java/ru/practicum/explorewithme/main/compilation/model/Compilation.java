package ru.practicum.explorewithme.main.compilation.model;

import lombok.*;
import ru.practicum.explorewithme.main.event.model.EventShortDto;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "pinned", nullable = false)
    private Boolean pinned;

    @Transient
    private List<EventShortDto> events;
}
