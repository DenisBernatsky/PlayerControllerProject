package com.playercontroller.tests;

import com.playercontroller.models.PlayerModel;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;

@Epic("PlayerController")
@Feature("Update Player")
public class TestUpdatePlayer extends PlayerCommon {

    @Test(description = "Verify that a player can be updated with all available fields")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensure that a supervisor can update a player’s data with all available fields (age, gender, login, password, screenName).")
    @Tag("positive")
    public void updatePlayerTest() {
        String suffix = String.valueOf(System.currentTimeMillis());

        // Step 1: Create a valid player
        PlayerModel request = PlayerModel.builder()
                .age(25)
                .gender("male")
                .login("validUser_" + suffix)
                .password("Pass1234")
                .role("user")
                .screenName("validScreen_" + suffix)
                .build();

        PlayerModel createdPlayer = createValidPlayer(request, "supervisor").as(PlayerModel.class);
        PlayerModel getPlayerResponse = playerSteps.get().getPlayerById(createdPlayer.getId()).as(PlayerModel.class);
        // Step 2: Prepare data for update (change all fields)
        PlayerModel updatedPlayer = PlayerModel.builder()
                .age(30)
                .gender("female")
                .login("updatedUser_" + suffix)
                .password("UpdatedPassword123")
                .role("admin")
                .screenName("updatedScreen_" + suffix)
                .build();

        // Step 3: Update the player with new data
        Response updateResponse = playerSteps.get().updatePlayer("supervisor", createdPlayer.getId(), updatedPlayer);

        // Assert that the status code is 200 for successful update
        assertEquals(updateResponse.getStatusCode(), 200, "Expected 200 status code for player update.");

        // Step 4: Retrieve the updated player and check that all fields are updated
        PlayerModel updatedPlayerResponse = playerSteps.get().getPlayerById(createdPlayer.getId()).as(PlayerModel.class);

        SoftAssert softAssert = new SoftAssert();
        // Validate that the entire updated player model is the same
        softAssert.assertEquals(updatedPlayerResponse, updatedPlayer, "The updated player data does not match the expected updated data.");

        // Also validate that ID is not changed after update
        softAssert.assertEquals(updatedPlayerResponse.getId(), getPlayerResponse.getId(), "ID should not be null after update.");
        softAssert.assertAll();
    }

    @Test(description = "Verify that a player cannot be updated with invalid data")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that a valid player cannot be updated with invalid data such as invalid age, role, password, and gender.")
    @Tag("negative")
    public void updatePlayerWithInvalidDataTest() {

        // Step 1: Create a valid player
        String suffix = String.valueOf(System.currentTimeMillis());
        PlayerModel validPlayer = PlayerModel.builder()
                .age(25)
                .gender("male")
                .login("validLogin_" + suffix)
                .password("ValidPass123")
                .role("admin")
                .screenName("validScreen_" + suffix)
                .build();

        PlayerModel createdPlayer = createValidPlayer(validPlayer, "supervisor").as(PlayerModel.class);

        // Initialize soft assert for detailed field comparison
        SoftAssert softAssert = new SoftAssert();

        // Step 2: Try to update with invalid age (less than 16)
        PlayerModel invalidAgePlayer = PlayerModel.builder()
                .age(15)  // Invalid age (too young)
                .gender("male")
                .login(createdPlayer.getLogin())
                .password("ValidPass123")
                .role("admin")
                .screenName(createdPlayer.getScreenName())
                .build();

        Response responseAge = playerSteps.get().updatePlayer("supervisor", createdPlayer.getId(), invalidAgePlayer);
        softAssert.assertEquals(responseAge.getStatusCode(), 400, "Expected error for invalid age.");

        // Step 3: Try to update with invalid role (not 'supervisor' or 'admin')
        PlayerModel invalidRolePlayer = PlayerModel.builder()
                .age(25)
                .gender("male")
                .login(createdPlayer.getLogin())
                .password("ValidPass123")
                .role("moderator")  // Invalid role
                .screenName(createdPlayer.getScreenName())
                .build();

        Response responseRole = playerSteps.get().updatePlayer("supervisor", createdPlayer.getId(), invalidRolePlayer);
        softAssert.assertEquals(responseRole.getStatusCode(), 403, "Expected error for invalid role.");

        // Step 4: Try to update with invalid gender (not 'male' or 'female')
        PlayerModel invalidGenderPlayer = PlayerModel.builder()
                .age(25)
                .gender("other")  // Invalid gender
                .login(createdPlayer.getLogin())
                .password("ValidPass123")
                .role("admin")
                .screenName(createdPlayer.getScreenName())
                .build();

        Response responseGender = playerSteps.get().updatePlayer("supervisor", createdPlayer.getId(), invalidGenderPlayer);
        softAssert.assertEquals(responseGender.getStatusCode(), 400, "Expected error for invalid gender.");

        // Step 5: Try to update with invalid password (too short)
        PlayerModel invalidPasswordPlayer = PlayerModel.builder()
                .age(25)
                .gender("male")
                .login(createdPlayer.getLogin())
                .password("123")  // Invalid password (too short)
                .role("admin")
                .screenName(createdPlayer.getScreenName())
                .build();

        Response responsePassword = playerSteps.get().updatePlayer("supervisor", createdPlayer.getId(), invalidPasswordPlayer);
        softAssert.assertEquals(responsePassword.getStatusCode(), 400, "Expected error for invalid password.");

        // Call assertAll() to verify all soft assertions
        softAssert.assertAll();
    }
}
