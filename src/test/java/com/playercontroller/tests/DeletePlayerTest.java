package com.playercontroller.tests;

import com.playercontroller.models.PlayerModel;
import com.playercontroller.steps.PlayerSteps;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Epic("PlayerController")
@Feature("Delete Player")
public class DeletePlayerTest extends PlayerCommon {

    @Test(description = "Verify successful deletion of player")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a supervisor can delete a player successfully.")
    @Tag("positive")
    public void deleteValidPlayerTest() {
        PlayerModel createdPlayer = createValidPlayer().as(PlayerModel.class);

        // Step 1: Retrieve the player data using the player ID
        Response responseGet = new PlayerSteps(spec).getPlayerById(createdPlayer.getId());
        assertEquals(responseGet.getStatusCode(), 200, "Expected 200 status code when retrieving player data.");

        // Step 2: Delete the player
        Response responseDelete = new PlayerSteps(spec).deletePlayer("supervisor", createdPlayer.getId());
        assertEquals(responseDelete.getStatusCode(), 204, "Expected 204 status code when deleting player data.");

        // Step 3: Verify the player is no longer available
        Response responseGetAfterDelete = new PlayerSteps(spec).getPlayerById(createdPlayer.getId());
        // Assert that the status code is 404 (Not Found) for a non-existent player
        assertEquals(responseGetAfterDelete.getStatusCode(), 404, "Expected 404 status code for non-existent player.");
    }


    @Test(description = "Verify successful deletion of player")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a supervisor can delete a player successfully.")
    @Tag("negative")
    public void deletePlayerTest() {
        PlayerModel createdPlayer = createValidPlayer().as(PlayerModel.class);

        // Step 1: Retrieve the player data using the player ID
        Response responseGet = new PlayerSteps(spec).getPlayerById(createdPlayer.getId());
        assertEquals(responseGet.getStatusCode(), 200, "Expected 200 status code when retrieving player data.");

        // Step 2: Delete the player
        Response responseDelete = new PlayerSteps(spec).deletePlayer("user", createdPlayer.getId());
        assertEquals(responseDelete.getStatusCode(), 403, "Expected 403 status code when deleting player data.");
    }
}
