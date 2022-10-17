package ru.practicum.explorewithme.main.ficha.location;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.ficha.location.model.Location;
import ru.practicum.explorewithme.main.ficha.location.model.LocationUpdate;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping("/admin/locations")
    public Location addLocation(@RequestBody @Valid Location location) {
        return locationService.addLocation(location);
    }

    @GetMapping("/admin/locations")
    public List<Location> getLocations() {
        return locationService.findLocations();
    }

    @PatchMapping("/admin/locations/{id}")
    public Location updateLocation(@PathVariable Long id,
                                   @RequestBody LocationUpdate locationUpdate) {
        return locationService.updateLocation(id, locationUpdate);
    }

    @PatchMapping("/admin/locations/{id}/approve")
    public Location approveLocation(@PathVariable Long id) {
        return locationService.approveLocation(id);
    }

    @PatchMapping("/admin/locations/{id}/reject")
    public Location rejectLocation(@PathVariable Long id) {
        return locationService.rejectLocation(id);
    }

    @PostMapping("/users/{userId}/locations")
    public Location userAddLocation(@PathVariable Long userId,
                                    @RequestBody @Valid Location location) {
        return locationService.userAddLocation(userId, location);
    }

}