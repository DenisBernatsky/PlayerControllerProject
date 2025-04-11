package com.playercontroller.tests;

import com.playercontroller.models.PlayerModel;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.playercontroller.utils.TestConstants.SUPERVISOR;
import static com.playercontroller.utils.TestConstants.USER;
import static org.testng.Assert.assertEquals;

@Epic("PlayerController")
@Feature("Delete Player")
public class TestDeletePlayer extends PlayerCommon {

    @Test(description = "Verify that a supervisor can delete a player successfully.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensure that a player can be deleted by a supervisor and is no longer retrievable afterward.")
    @Tag("positive")
    public void deleteValidPlayerTest() {
        Allure.step("Step 1: Create a valid player.");
        PlayerModel createdPlayer = createValidPlayer().as(PlayerModel.class);

        Allure.step("Step 2: Retrieve the player data using the player ID");
        Response responseGet = playerSteps.get().getPlayerById(createdPlayer.getId());
        assertEquals(responseGet.getStatusCode(), 200, "Player should be retrievable before deletion.");

        Allure.step("Step 3: Delete the player with supervisor role");
        Response responseDelete = playerSteps.get().deletePlayer(SUPERVISOR, createdPlayer.getId());
        assertEquals(responseDelete.getStatusCode(), 204, "Expected successful deletion (204 No Content).");

        Allure.step("Step 4: Verify the player is no longer available");
        Response responseGetAfterDelete = playerSteps.get().getPlayerById(createdPlayer.getId());
        assertEquals(responseGetAfterDelete.getStatusCode(), 404, "Deleted player should not be found (404 Not Found).");
    }

    @Test(description = "Verify that a regular user is forbidden from deleting a player.")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensure that a user without sufficient privileges cannot delete a player.")
    @Tag("negative")
    public void deletePlayerNotAllowedTest() {
        Allure.step("Step 1: Create a valid player.");
        PlayerModel createdPlayer = createValidPlayer().as(PlayerModel.class);

        Allure.step("Step 2: Retrieve the player data using the player ID");
        Response responseGet = playerSteps.get().getPlayerById(createdPlayer.getId());
        assertEquals(responseGet.getStatusCode(), 200, "Player should be retrievable before deletion attempt.");

        Allure.step("Step 3: Attempt to delete the player using user role");
        Response responseDelete = playerSteps.get().deletePlayer(USER, createdPlayer.getId());
        assertEquals(responseDelete.getStatusCode(), 403, "User should not have permission to delete a player (403 Forbidden).");
    }
}
