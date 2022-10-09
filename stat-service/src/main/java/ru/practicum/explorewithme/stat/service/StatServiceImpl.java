package ru.practicum.explorewithme.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.stat.model.EndpointHit;
import ru.practicum.explorewithme.stat.exception.NotFoundException;
import ru.practicum.explorewithme.stat.repository.StatRepository;
import ru.practicum.explorewithme.stat.model.ViewStats;
import org.modelmapper.ModelMapper;
import ru.practicum.explorewithme.statmodule.model.ViewStatsModule;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {
    private final StatRepository statsRepository;

    private final ModelMapper mapper;

    @Override
    public void addStat(EndpointHit endpointHit) {
        statsRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsModule> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<Long> ids;

        if (uris == null || uris.isEmpty()) {
            ids = statsRepository.getIds(start, end);
        } else {
            ids = statsRepository.getIdsWithUris(start, end, uris);
        }

        if (ids.isEmpty()) {
            throw new NotFoundException("Не найдены записи");
        }

        List<ViewStats> viewStats;

        if (unique == null || !unique) {
            viewStats = statsRepository.getViewStats(ids);
        } else {
            viewStats = statsRepository.getViewStatsWithUniqueIp(ids);
        }

        return viewStats.stream()
            .map(viewStat -> mapper.map(viewStat, ViewStatsModule.class))
            .collect(Collectors.toList());
    }
}
