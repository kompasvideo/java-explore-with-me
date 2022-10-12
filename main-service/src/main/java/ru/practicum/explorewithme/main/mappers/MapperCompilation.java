package ru.practicum.explorewithme.main.mappers;

import lombok.RequiredArgsConstructor;
import ru.practicum.explorewithme.main.compilation.model.EventCompilation;

@RequiredArgsConstructor
public class MapperCompilation {
    public static EventCompilation mapEventCompilation(Long eventId, Long compId) {
        return EventCompilation.builder()
                .eventId(eventId)
                .compilationId(compId)
                .build();
    }
}
