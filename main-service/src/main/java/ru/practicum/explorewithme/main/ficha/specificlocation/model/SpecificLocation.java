package ru.practicum.explorewithme.main.ficha.specificlocation.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "locations")
@Setter
@Getter
public class SpecificLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
    @NotNull
    private Float radius;
    @Enumerated(EnumType.STRING)
    private Status status;
}
