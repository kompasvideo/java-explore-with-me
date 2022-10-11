package ru.practicum.explorewithme.main.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.main.compilation.model.EventCompilation;

import java.util.List;

public interface CompilationEventRepository extends JpaRepository<EventCompilation, Long> {
    Boolean existsByEventIdAndCompilationId(Long eventId, Long compId);

    @Query("select e.id from EventCompilation e where e.compilationId = ?2 and e.eventId = ?1")
    Long findEventIdByEventIdAndCompilationId(Long eventId, Long compId);

    @Query("select c.id from EventCompilation c where c.compilationId = ?1")
    List<Long> findAllByCompilationId(Long compId);
}
