package org.solvd.recommendation.model;


import lombok.*;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRating {
    private Long movieId;
    private Long userId;
    private BigDecimal ratingValue;
}
