package org.solvd.recommendation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferredGenre {
    private Long genreId;
    private Long userId;
}
