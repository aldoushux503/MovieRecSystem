package org.solvd.recommendation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    private Long genreId;
    private String name;
}
