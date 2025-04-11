package com.playercontroller.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerModel {
    private int age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;
    private Long id;
}