package ru.practicum.explorewithme.stat.service;

import ru.practicum.explorewithme.stat.model.EndpointHit;
import ru.practicum.explorewithme.statmodule.model.ViewStatsModule;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    void addStat(EndpointHit endpointHit);

    List<ViewStatsModule> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
