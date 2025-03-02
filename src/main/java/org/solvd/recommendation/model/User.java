package org.solvd.recommendation.model;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Person {
    private Long userId;
    private String username;
    private String email;
}
