package org.solvd.recommendation.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Long personId;
    private String fullName;
    private String email;
}
