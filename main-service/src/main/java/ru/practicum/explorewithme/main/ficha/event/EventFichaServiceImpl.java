package ru.practicum.explorewithme.main.ficha.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.explorewithme.main.event.model.*;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.exception.ValidationException;
import ru.practicum.explorewithme.main.ficha.specificlocation.SpecificLocationRepository;
import ru.practicum.explorewithme.main.ficha.specificlocation.model.SpecificLocation;
import ru.practicum.explorewithme.main.ficha.specificlocation.model.Status;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventFichaServiceImpl {
    private final EventFichaRepository eventRepository;
    private final SpecificLocationRepository specificLocationRepository;

    public BooleanExpression getBooleanExpression(BooleanExpression basedExpression, String locationName) {
        if (StringUtils.hasText(locationName)) {
            if (!specificLocationRepository.existsByName(locationName)) {
                throw new NotFoundException("Не найдена локация");
            }

            SpecificLocation specificLocation = specificLocationRepository.findSpecificLocationByName(locationName);

            if (!specificLocation.getStatus().equals(Status.APPROVED)) {
                throw new ValidationException("Недопустимая локация");
            }

            List<Long> ids = eventRepository.findAllIdsByLocation(specificLocation.getLon(),
                specificLocation.getLat(),
                specificLocation.getRadius());

            basedExpression = basedExpression.and(QEvent.event.id.in(ids));
        }
        return basedExpression;
    }

    public BooleanExpression getBooleanExpression2(BooleanExpression basedExpression, String locationName) {
        if (StringUtils.hasText(locationName)) {
            if (!specificLocationRepository.existsByName(locationName)) {
                throw new NotFoundException("Не найдена локация");
            }

            SpecificLocation specificLocation = specificLocationRepository.findSpecificLocationByName(locationName);

            if (!specificLocation.getStatus().equals(Status.APPROVED)) {
                throw new ValidationException("Недопустимая локация");
            }

            List<Long> ids = eventRepository.findAllIdsByLocation(specificLocation.getLon(),
                specificLocation.getLat(),
                specificLocation.getRadius());

            basedExpression = basedExpression.and(QEvent.event.id.in(ids));
        }
        return basedExpression;
    }


}
