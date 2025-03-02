package org.solvd.recommendation.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Long personId;
    private String fullName;
    private String email;
}
