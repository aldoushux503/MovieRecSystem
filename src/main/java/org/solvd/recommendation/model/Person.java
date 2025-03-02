package org.solvd.recommendation.model;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Long personId;
    private String fullName;
    private String email;
}
