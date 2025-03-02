package org.solvd.recommendation.model;


import lombok.*;

import java.sql.Time;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ViewingHistory {
    private Long movieId;
    private Long userId;
    private Time watchDate;
}
