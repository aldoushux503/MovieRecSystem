package org.solvd.recommendation.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private Long movieId;
    private String title;
    private Integer duration;
    private BigDecimal averageRating;
}
