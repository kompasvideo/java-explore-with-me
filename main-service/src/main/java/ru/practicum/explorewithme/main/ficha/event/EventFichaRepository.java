package ru.practicum.explorewithme.main.ficha.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.explorewithme.main.event.model.Event;

import java.util.List;

public interface EventFichaRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    @Query("select e.id " +
        "from Event e " +
        "where ((e.lon - ?1) * (e.lon - ?1) + (e.lat - ?2) * (e.lat - ?2)) < (?3 * ?3)")
    List<Long> findAllIdsByLocation(Float lon, Float lat, Float radius);

}
