package ru.practicum.explorewithme.stat.model;

import lombok.*;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@NamedNativeQuery(
        name = "find_view_stats_with_unique_ip",
        query =
                "select e.app as app, e.uri as uri, count(e.ip) as hits " +
                        "from (" +
                        "   select app, uri, ip " +
                        "   from stat_events " +
                        "   where id in ?1" +
                        "   group by app, uri, ip" +
                        ") as e " +
                        "group by e.app, e.uri",
        resultSetMapping = "view_stats"
)
@NamedNativeQuery(
        name = "find_view_stats",
        query =
                "select app as app, uri as uri, count(ip) as hits " +
                        "from stat_events " +
                        "where id in ?1 " +
                        "group by app, uri",
        resultSetMapping = "view_stats"
)
@SqlResultSetMapping(
        name = "view_stats",
        classes = @ConstructorResult(
                targetClass = ViewStats.class,
                columns = {
                        @ColumnResult(name = "app", type = String.class),
                        @ColumnResult(name = "uri", type = String.class),
                        @ColumnResult(name = "hits", type = Long.class)
                }
        )
)
public class ViewStats {
    @Id
    private String app;
    private String uri;
    private Long hits;
}
