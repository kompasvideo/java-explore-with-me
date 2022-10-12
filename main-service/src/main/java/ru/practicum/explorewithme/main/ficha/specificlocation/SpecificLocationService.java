package ru.practicum.explorewithme.main.ficha.specificlocation;

import ru.practicum.explorewithme.main.ficha.specificlocation.model.SpecificLocation;
import ru.practicum.explorewithme.main.ficha.specificlocation.model.SpecificLocationUpdate;


import java.util.List;

public interface SpecificLocationService {
    SpecificLocation addLocation(SpecificLocation specificLocation);

    List<SpecificLocation> getLocations();

    SpecificLocation userAddLocation(Long userId, SpecificLocation specificLocation);

    SpecificLocation approveLocation(Long locId);

    SpecificLocation rejectLocation(Long locId);

    SpecificLocation updateLocation(Long locId, SpecificLocationUpdate specificLocationUpdate);
}

