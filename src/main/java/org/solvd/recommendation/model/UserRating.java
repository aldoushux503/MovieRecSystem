package org.solvd.recommendation.model;


import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserRating {
    private Long movieId;
    private Long userId;
    private Double ratingValue;
}
