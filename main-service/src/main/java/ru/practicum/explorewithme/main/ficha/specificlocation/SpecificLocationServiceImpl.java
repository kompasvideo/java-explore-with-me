package ru.practicum.explorewithme.main.ficha.specificlocation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.exception.ValidationException;
import ru.practicum.explorewithme.main.ficha.specificlocation.model.SpecificLocation;
import ru.practicum.explorewithme.main.ficha.specificlocation.model.SpecificLocationUpdate;
import ru.practicum.explorewithme.main.ficha.specificlocation.model.Status;
import ru.practicum.explorewithme.main.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecificLocationServiceImpl implements SpecificLocationService {
    private final SpecificLocationRepository specificLocationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SpecificLocation addLocation(SpecificLocation specificLocation) {
        if (specificLocationRepository.existsByName(specificLocation.getName())) {
            throw new ValidationException("Такая локация уже существует");
        }

        specificLocation.setStatus(Status.APPROVED);

        return specificLocationRepository.save(specificLocation);
    }

    @Override
    public List<SpecificLocation> getLocations() {
        return specificLocationRepository.findAll();
    }

    @Override
    @Transactional
    public SpecificLocation userAddLocation(Long userId, SpecificLocation specificLocation) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с заданным id не найден");
        }

        if (specificLocationRepository.existsByName(specificLocation.getName())) {
            throw new ValidationException("Такая локация уже существует");
        }

        specificLocation.setStatus(Status.WAITING);

        return specificLocationRepository.save(specificLocation);
    }

    @Override
    @Transactional
    public SpecificLocation approveLocation(Long locId) {
        if (!specificLocationRepository.existsById(locId)) {
            throw new NotFoundException("Локация не найдена");
        }

        SpecificLocation specificLocation = specificLocationRepository.getReferenceById(locId);

        if (!specificLocation.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Локация не может быть одобрена");
        }

        specificLocation.setStatus(Status.APPROVED);

        return specificLocationRepository.save(specificLocation);
    }

    @Override
    @Transactional
    public SpecificLocation rejectLocation(Long locId) {
        if (!specificLocationRepository.existsById(locId)) {
            throw new NotFoundException("Локация не найдена");
        }

        SpecificLocation specificLocation = specificLocationRepository.getReferenceById(locId);

        if (!specificLocation.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Локация не может быть одобрена");
        }

        specificLocation.setStatus(Status.REJECTED);

        return specificLocationRepository.save(specificLocation);
    }

    @Override
    @Transactional
    public SpecificLocation updateLocation(Long locId, SpecificLocationUpdate specificLocationUpdate) {
        if (!specificLocationRepository.existsById(locId)) {
            throw new NotFoundException("Локация не найдена");
        }

        SpecificLocation specificLocation = specificLocationRepository.getReferenceById(locId);

        if (specificLocation.getStatus().equals(Status.REJECTED)) {
            throw new ValidationException("Событие не может быть изменено");
        }

        if (specificLocationUpdate.getName() != null) {
            if (specificLocationRepository.existsByName(specificLocationUpdate.getName())) {
                throw new NotFoundException("Такое имя уже существует");
            }

            specificLocation.setName(specificLocationUpdate.getName());
        }

        if (specificLocationUpdate.getLat() != null) {
            specificLocation.setLat(specificLocationUpdate.getLat());
        }

        if (specificLocationUpdate.getLon() != null) {
            specificLocation.setLon(specificLocationUpdate.getLon());
        }

        if (specificLocationUpdate.getRadius() != null) {
            specificLocation.setRadius(specificLocationUpdate.getRadius());
        }

        return specificLocationRepository.save(specificLocation);
    }
}

