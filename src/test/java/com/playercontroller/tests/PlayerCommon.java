package com.playercontroller.tests;

import com.playercontroller.base.BaseTest;
import com.playercontroller.models.PlayerModel;
import com.playercontroller.steps.PlayerSteps;
import io.restassured.response.Response;

import static com.playercontroller.utils.TestConstants.*;
import static org.testng.Assert.assertEquals;

public class PlayerCommon extends BaseTest {
    protected final ThreadLocal<PlayerSteps> playerSteps = ThreadLocal.withInitial(PlayerSteps::new);

    protected Response createValidPlayer(PlayerModel request, String editor) {

        // Make the request and get the response
        Response apiResponse = playerSteps.get().createPlayer(editor, request);

        // Assert the response status code is 200 (successful creation)
        assertEquals(apiResponse.getStatusCode(), 200, "Expected successful user creation, but got error.");
        return apiResponse;
    }

    protected Response createValidPlayer() {
        String suffix = String.valueOf(System.currentTimeMillis());

        // Create a request with valid data
        PlayerModel request = PlayerModel.builder()
                .age(25)
                .gender(GENDER_MALE)
                .login("validUser_" + suffix)
                .password("Pass1234")
                .role(ADMIN)
                .screenName("validScreen_" + suffix)
                .build();
        return createValidPlayer(request, SUPERVISOR);
    }
}
