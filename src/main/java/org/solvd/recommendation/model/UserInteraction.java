package org.solvd.recommendation.model;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserInteraction {
    private Long movieId;
    private Long userId;
    private Long interactionId;
}
