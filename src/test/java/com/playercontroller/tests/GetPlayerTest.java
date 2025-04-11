package com.playercontroller.tests;

import com.playercontroller.models.PlayerModel;
import com.playercontroller.steps.PlayerSteps;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

@Epic("PlayerController")
@Feature("Get Player")
public class GetPlayerTest extends PlayerCommon {

    @Test(description = "Verify that a valid player can be retrieved successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensure that a supervisor can retrieve the data of a valid player using the player's ID.")
    @Tag("positive")
    public void retrieveValidPlayerTest() {

        String suffix = String.valueOf(System.currentTimeMillis());

        //Create a player
        PlayerModel request = PlayerModel.builder()
                .age(25)
                .gender("male")
                .login("validUser_" + suffix)  // Unique login
                .password("Pass1234")
                .role("admin")
                .screenName("validScreen_" + suffix)  // Unique screenName
                .build();

        // Step 1: Create a player using the common method
        PlayerModel createdPlayer =  createValidPlayer(request, "supervisor").as(PlayerModel.class);

        // Step 2: Retrieve the player data using the player ID
        Response response = new PlayerSteps(spec).getPlayerById(createdPlayer.getId());

        assertEquals(response.getStatusCode(), 200, "Expected 200 status code when retrieving player data.");
        assertEquals(response.as(PlayerModel.class), request,"The player data retrieved does not match the player data that was created.");
    }

    @Test(description = "Verify that an invalid player cannot be retrieved")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensure that a supervisor cannot retrieve a player using a non-existent player ID.")
    @Tag("negative")
    public void retrieveInvalidPlayerTest() {

        // Use a non-existent player ID
        Long invalidPlayerId = 999999999L;

        // Step 1: Attempt to retrieve the player data using the invalid player ID
        Response response = new PlayerSteps(spec).getPlayerById(invalidPlayerId);

        // Assert that the status code is 404 (Not Found) for a non-existent player
        assertEquals(response.getStatusCode(), 404, "Expected 404 status code for non-existent player.");
    }
}
