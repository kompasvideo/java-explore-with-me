package ru.practicum.explorewithme.main.compilation;

import ru.practicum.explorewithme.main.compilation.model.Compilation;
import ru.practicum.explorewithme.main.compilation.model.CompilationDto;

import java.util.List;

public interface CompilationService {
    Compilation addCompilation(CompilationDto compilationDto);

    void deleteCompilation(Long compId);

    void deleteById(Long compId, Long eventId);

    void addEvent(Long compId, Long eventId);

    void unpin(Long compId);

    void pin(Long compId);

    List<Compilation> findAll(Boolean pinned, Integer from, Integer size);

    Compilation findById(Long compId);
}
