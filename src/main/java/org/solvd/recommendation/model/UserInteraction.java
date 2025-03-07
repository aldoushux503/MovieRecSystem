package org.solvd.recommendation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInteraction {
    private Long movieId;
    private Long userId;
    private Long interactionsId;
}
