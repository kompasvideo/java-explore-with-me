package ru.practicum.explorewithme.stat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.stat.model.EndpointHit;
import ru.practicum.explorewithme.stat.service.StatServiceImpl;
import ru.practicum.explorewithme.statmodule.model.ViewStatsModule;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatServiceImpl statisticService;

    @PostMapping("/hit")
    public void addStat(@RequestBody @Valid EndpointHit endpointHit) {
        statisticService.addStat(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStatsModule> getStats(@RequestParam LocalDateTime start,
                                          @RequestParam LocalDateTime end,
                                          @RequestParam(required = false) List<String> uris,
                                          @RequestParam(required = false) Boolean unique) {
        return statisticService.getStats(start, end, uris, unique);
    }
}
