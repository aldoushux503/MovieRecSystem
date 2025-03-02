package org.solvd.recommendation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonRole {
    private Long personRoleId;
    private String roleName;
}
