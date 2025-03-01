package org.solvd.recommendation.model;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ContentContributor {
    private Long movieId;
    private Long personId;
    private Integer personRoleId;
}
