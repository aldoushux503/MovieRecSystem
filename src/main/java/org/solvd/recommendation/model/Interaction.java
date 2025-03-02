package org.solvd.recommendation.model;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Interaction {
    private Long interactionId;
    private String type;
}
