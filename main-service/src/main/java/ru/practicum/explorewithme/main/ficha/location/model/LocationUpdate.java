package ru.practicum.explorewithme.main.ficha.location.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationUpdate {
    private String name;
    private Float lat;
    private Float lon;
    private Float radius;
}
