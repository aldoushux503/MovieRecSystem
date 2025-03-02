package org.solvd.recommendation.model;


import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewingHistory {
    private Long movieId;
    private Long userId;
    private Timestamp watchDate;
}
