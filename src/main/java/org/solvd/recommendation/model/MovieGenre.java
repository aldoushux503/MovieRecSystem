package org.solvd.recommendation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieGenre {
    private Long movieId;
    private Long genreId;
}
