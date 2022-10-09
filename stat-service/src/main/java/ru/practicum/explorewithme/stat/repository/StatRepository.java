package ru.practicum.explorewithme.stat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.stat.model.EndpointHit;
import ru.practicum.explorewithme.stat.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {
    @Query(nativeQuery = true, name = "find_view_stats_with_unique_ip")
    List<ViewStats> getViewStatsWithUniqueIp(List<Long> ids);

    @Query(nativeQuery = true, name = "find_view_stats")
    List<ViewStats> getViewStats(List<Long> ids);

    @Query("select e.id " +
        "from EndpointHit e " +
        "where e.timestamp > ?1 and e.timestamp < ?2 and e.uri in ?3")
    List<Long> getIdsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select e.id " +
        "from EndpointHit e " +
        "where e.timestamp > ?1 and e.timestamp < ?2")
    List<Long> getIds(LocalDateTime start, LocalDateTime end);
}
