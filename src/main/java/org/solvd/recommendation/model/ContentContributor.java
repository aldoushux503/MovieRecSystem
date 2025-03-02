package org.solvd.recommendation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentContributor {
    private Long movieId;
    private Long personId;
    private Integer personRoleId;
}
