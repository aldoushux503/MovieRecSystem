package org.solvd.recommendation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interaction {
    private Long interactionId;
    private InteractionType type;
}
