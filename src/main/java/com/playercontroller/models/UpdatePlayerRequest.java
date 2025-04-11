package com.playercontroller.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlayerRequest {
    private Integer age;
    private String gender;
    private String login;
    private String password;
    private String screenName;
}
