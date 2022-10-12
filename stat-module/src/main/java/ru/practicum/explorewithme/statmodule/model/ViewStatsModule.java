package ru.practicum.explorewithme.statmodule.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ViewStatsModule {
    private String app;
    private String uri;
    private Long hits;
}
