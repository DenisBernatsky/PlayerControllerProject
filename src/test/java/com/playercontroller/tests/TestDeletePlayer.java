package com.playercontroller.tests;

import com.playercontroller.models.PlayerModel;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.concurrent.ThreadLocalRandom;

import static com.playercontroller.utils.TestConstants.*;
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

    @Test(description = "Verify that an admin can delete a user successfully.")
    @Severity(SeverityLevel.NORMAL)
    @Description("Ensure that a user with role 'user' can be deleted by an admin.")
    @Tag("positive")
    public void adminDeletesUserTest() {
        Allure.step("Step 1: Create a user with role 'user' via supervisor");
        PlayerModel userToDelete = PlayerModel.builder()
                .age(ThreadLocalRandom.current().nextInt(17, 60))
                .gender(GENDER_MALE)
                .login("userToDelete_" + System.currentTimeMillis())
                .password("Password123")
                .role(USER)
                .screenName("screen_" + System.currentTimeMillis())
                .build();

        PlayerModel createdPlayer = createValidPlayer(userToDelete, SUPERVISOR).as(PlayerModel.class);

        Allure.step("Step 2: Admin attempts to delete the user");
        Response deleteResponse = playerSteps.get().deletePlayer(ADMIN, createdPlayer.getId());

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(deleteResponse.getStatusCode(), 204,"Expected 204 No Content when admin deletes user");

        Allure.step("Step 3: Verify the user no longer exists");
        Response getAfterDelete = playerSteps.get().getPlayerById(createdPlayer.getId());
        softAssert.assertEquals(getAfterDelete.getStatusCode(), 404,"Expected 404 Not Found for deleted user");
        softAssert.assertAll();
    }

    @Test(description = "Verify that an admin cannot delete another admin.")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensure that an admin is not allowed to delete another user with the admin role.")
    @Tag("negative")
    public void adminCannotDeleteAdminTest() {
        Allure.step("Step 1: Create a user with role 'admin' via supervisor");
        PlayerModel adminToDelete = PlayerModel.builder()
                .age(ThreadLocalRandom.current().nextInt(17, 60))
                .gender(GENDER_FEMALE)
                .login("adminToDelete_" + System.currentTimeMillis())
                .password("SecurePass456")
                .role(ADMIN)
                .screenName("adminScreen_" + System.currentTimeMillis())
                .build();

        PlayerModel createdAdmin = createValidPlayer(adminToDelete, SUPERVISOR).as(PlayerModel.class);

        Allure.step("Step 2: Admin attempts to delete another admin");
        Response deleteResponse = playerSteps.get().deletePlayer(ADMIN, createdAdmin.getId());

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(deleteResponse.getStatusCode(), 403,
                "Expected 403 Forbidden when admin tries to delete another admin.");

        Allure.step("Step 3: Verify that the admin still exists after failed deletion");
        Response getAfterDeleteAttempt = playerSteps.get().getPlayerById(createdAdmin.getId());
        softAssert.assertEquals(getAfterDeleteAttempt.getStatusCode(), 200,
                "Expected 200 OK — the admin should still be present in the system.");

        softAssert.assertAll();
    }
}

