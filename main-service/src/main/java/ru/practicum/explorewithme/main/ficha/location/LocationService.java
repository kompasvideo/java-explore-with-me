package ru.practicum.explorewithme.main.ficha.location;

import ru.practicum.explorewithme.main.ficha.location.model.Location;
import ru.practicum.explorewithme.main.ficha.location.model.LocationUpdate;


import java.util.List;

public interface LocationService {
    Location addLocation(Location location);

    List<Location> findLocations();

    Location userAddLocation(Long userId, Location location);

    Location approveLocation(Long locId);

    Location rejectLocation(Long locId);

    Location updateLocation(Long locId, LocationUpdate locationUpdate);
}

