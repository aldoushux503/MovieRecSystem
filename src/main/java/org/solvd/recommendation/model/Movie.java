package org.solvd.recommendation.model;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    private Long movieId;
    private String title;
    private Integer duration;
    private Double averageRating;
}
