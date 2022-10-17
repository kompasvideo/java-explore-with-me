package ru.practicum.explorewithme.main.ficha.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.exception.ValidationException;
import ru.practicum.explorewithme.main.ficha.location.model.Location;
import ru.practicum.explorewithme.main.ficha.location.model.LocationUpdate;
import ru.practicum.explorewithme.main.ficha.location.model.Status;
import ru.practicum.explorewithme.main.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public Location addLocation(Location location) {
        if (locationRepository.existsByNameIgnoreCase(location.getName())) {
            throw new ValidationException("Такая локация уже существует");
        }

        location.setStatus(Status.APPROVED);

        return locationRepository.save(location);
    }

    @Override
    public List<Location> findLocations() {
        return locationRepository.findAll();
    }

    @Override
    @Transactional
    public Location userAddLocation(Long userId, Location location) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с заданным id не найден");
        }

        if (locationRepository.existsByNameIgnoreCase(location.getName())) {
            throw new ValidationException("Такая локация уже существует");
        }

        location.setStatus(Status.WAITING);

        return locationRepository.save(location);
    }

    @Override
    @Transactional
    public Location approveLocation(Long locId) {
        if (!locationRepository.existsById(locId)) {
            throw new NotFoundException("Локация не найдена");
        }

        Location location = locationRepository.getReferenceById(locId);

        if (!location.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Локация не может быть одобрена");
        }

        location.setStatus(Status.APPROVED);

        return locationRepository.save(location);
    }

    @Override
    @Transactional
    public Location rejectLocation(Long locId) {
        if (!locationRepository.existsById(locId)) {
            throw new NotFoundException("Локация не найдена");
        }

        Location location = locationRepository.getReferenceById(locId);

        if (!location.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Локация не может быть отклонена");
        }

        location.setStatus(Status.REJECTED);

        return locationRepository.save(location);
    }

    @Override
    @Transactional
    public Location updateLocation(Long locId, LocationUpdate locationUpdate) {
        if (!locationRepository.existsById(locId)) {
            throw new NotFoundException("Локация не найдена");
        }

        Location location = locationRepository.getReferenceById(locId);

        if (location.getStatus().equals(Status.REJECTED)) {
            throw new ValidationException("Событие не может быть изменено");
        }

        if (locationUpdate.getName() != null) {
            if (locationRepository.existsByNameIgnoreCase(locationUpdate.getName())) {
                throw new NotFoundException("Такое имя уже существует");
            }

            location.setName(locationUpdate.getName());
        }

        if (locationUpdate.getLat() != null) {
            location.setLat(locationUpdate.getLat());
        }

        if (locationUpdate.getLon() != null) {
            location.setLon(locationUpdate.getLon());
        }

        if (locationUpdate.getRadius() != null) {
            location.setRadius(locationUpdate.getRadius());
        }

        return locationRepository.save(location);
    }
}

