package ru.practicum.explorewithme.main.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.compilation.model.Compilation;
import ru.practicum.explorewithme.main.compilation.model.CompilationDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    public Compilation addCompilation(@RequestBody @Valid CompilationDto compilationDto) {
        log.info("Добавить подборку событий {}", compilationDto.toString());
        return compilationService.addCompilation(compilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteById(@PathVariable Long compId,
                           @PathVariable Long eventId) {
        log.info("Удалить подборку событий с id: {}", compId);
        compilationService.deleteById(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addEvent(@PathVariable Long compId,
                         @PathVariable Long eventId) {
        log.info("Добавить событие с eventId: {} в подборку событий с compId: {}", eventId, compId);
        compilationService.addEvent(compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void unpin(@PathVariable Long compId) {
        log.info("Удалить подборку с id: {}", compId);
        compilationService.unpin(compId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void pin(@PathVariable Long compId) {
        log.info("Добавить подборку с id: {}", compId);
        compilationService.pin(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Удалить подборку событий с id: {}", compId);
        compilationService.deleteCompilation(compId);
    }
}
