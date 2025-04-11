package com.playercontroller.tests;

import com.playercontroller.models.PlayerModel;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.playercontroller.utils.TestConstants.*;
import static org.testng.Assert.assertEquals;

@Epic("PlayerController")
@Feature("Get Player")
public class TestGetPlayer extends PlayerCommon {

    @Test(description = "Verify that a valid player can be retrieved successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensure that a supervisor can retrieve the data of a valid player using the player's ID.")
    @Tag("positive")
    public void retrieveValidPlayerTest() {

        String suffix = String.valueOf(System.currentTimeMillis());

        // Create a player
        PlayerModel request = PlayerModel.builder()
                .age(25)
                .gender(GENDER_MALE)
                .login("validUser_" + suffix)
                .password("Pass1234")
                .role(ADMIN)
                .screenName("validScreen_" + suffix)
                .build();

        Allure.step("Step 1: Create a valid player");
        PlayerModel createdPlayer = createValidPlayer(request, SUPERVISOR).as(PlayerModel.class);

        Allure.step("Step 2: Retrieve the player data using the player ID");
        Response response = playerSteps.get().getPlayerById(createdPlayer.getId());

        Allure.step("Step 3: Assert the response status code is 200 and data matches.");
        assertEquals(response.getStatusCode(), 200, "Expected 200 status code when retrieving player data.");
        assertEquals(response.as(PlayerModel.class), request, "The player data retrieved does not match the player data that was created.");
    }

    @Test(description = "Verify that an invalid player cannot be retrieved")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensure that a supervisor cannot retrieve a player using a non-existent player ID.")
    @Tag("negative")
    public void retrieveInvalidPlayerTest() {

        Allure.step("Step 1: Attempt to retrieve the player data using the invalid player ID");
        Response response = playerSteps.get().getPlayerById(NON_EXISTENT_PLAYER_ID);

        Allure.step("Step 2: Assert the response status code is 404");
        assertEquals(response.getStatusCode(), 404, "Expected 404 status code for non-existent player.");
    }
}
