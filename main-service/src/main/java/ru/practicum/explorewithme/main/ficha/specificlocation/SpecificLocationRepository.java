package ru.practicum.explorewithme.main.ficha.specificlocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.main.ficha.specificlocation.model.SpecificLocation;

import java.util.List;

public interface SpecificLocationRepository extends JpaRepository<SpecificLocation, Long> {
    @Query("select case when (count(l)) > 0 then true else false end " +
        "from SpecificLocation l " +
        "where l.name = ?1")
    Boolean existsByName(String name);

    SpecificLocation findSpecificLocationByName(String locationName);

    @Query(nativeQuery = true,
        value = "select t.name from (" +
            "   select name, (lon - ?1) * (lon - ?1) + (lat - ?2) * (lat - ?2) as dist " +
            "   from locations " +
            "   where (lon - ?1) * (lon - ?1) + (lat - ?2) * (lat - ?2) < radius * radius " +
            "       and status = 'APPROVED'" +
            "   except " +
            "   select s.name, s.dist " +
            "   from (" +
            "       select name, (lon - ?1) * (lon - ?1) + (lat - ?2) * (lat - ?2) as dist " +
            "       from locations " +
            "       where (lon - ?1) * (lon - ?1) + (lat - ?2) * (lat - ?2) < radius * radius) as f " +
            "       left join (" +
            "           select name, (lon - ?1) * (lon - ?1) + (lat - ?2) * (lat - ?2) as dist " +
            "           from locations " +
            "           where (lon - ?1) * (lon - ?1) + (lat - ?2) * (lat - ?2) < radius * radius) as s " +
            "           on f.dist < s.dist) as t")
    List<String> findNearestLocation(Float lon, Float lat);
}

