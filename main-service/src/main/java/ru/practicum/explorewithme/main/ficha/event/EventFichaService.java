package ru.practicum.explorewithme.main.ficha.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.explorewithme.main.event.model.*;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.exception.ValidationException;
import ru.practicum.explorewithme.main.ficha.location.LocationRepository;
import ru.practicum.explorewithme.main.ficha.location.model.Location;
import ru.practicum.explorewithme.main.ficha.location.model.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventFichaService {
    private final EventFichaRepository eventRepository;
    private final LocationRepository locationRepository;

    public BooleanExpression getBooleanExpression(BooleanExpression basedExpression, String locationName) {
        if (StringUtils.hasText(locationName)) {
            if (!locationRepository.existsByNameIgnoreCase(locationName)) {
                throw new NotFoundException("Не найдена локация");
            }

            Location location = locationRepository.findByNameIgnoreCase(locationName);

            if (!location.getStatus().equals(Status.APPROVED)) {
                throw new ValidationException("Недопустимая локация");
            }

            List<Long> ids = eventRepository.findAllIdsByLocation(location.getLon(),
                location.getLat(),
                location.getRadius());

            basedExpression = basedExpression.and(QEvent.event.id.in(ids));
        }
        return basedExpression;
    }
}
