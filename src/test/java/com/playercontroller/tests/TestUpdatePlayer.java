package com.playercontroller.tests;

import com.playercontroller.models.PlayerModel;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.playercontroller.utils.TestConstants.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

@Epic("PlayerController")
@Feature("Update Player")
public class TestUpdatePlayer extends PlayerCommon {

    @Test(description = "Verify that a player can be updated with all available fields")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Ensure that a supervisor can update a player’s data with all available fields (age, gender, login, password, screenName).")
    @Tag("positive")
    public void updatePlayerTest() {

        Allure.step("Step 1: Create a valid player");
        String suffix = String.valueOf(System.currentTimeMillis());
        PlayerModel request = PlayerModel.builder()
                .age(25)
                .gender(GENDER_MALE)
                .login("validUser_" + suffix)
                .password("Pass1234")
                .role(USER)
                .screenName("validScreen_" + suffix)
                .build();

        PlayerModel createdPlayer = createValidPlayer(request, SUPERVISOR).as(PlayerModel.class);
        PlayerModel getPlayerResponse = playerSteps.get().getPlayerById(createdPlayer.getId()).as(PlayerModel.class);

        Allure.step("Step 2: Prepare data for update (change all fields)");
        PlayerModel updatedPlayer = PlayerModel.builder()
                .age(30)
                .gender(GENDER_FEMALE)
                .login("updatedUser_" + suffix)
                .password("UpdatedPassword123")
                .role(ADMIN)
                .screenName("updatedScreen_" + suffix)
                .build();

        Allure.step("Step 3: Update the player with new data");
        Response updateResponse = playerSteps.get().updatePlayer(SUPERVISOR, createdPlayer.getId(), updatedPlayer);
        assertEquals(updateResponse.getStatusCode(), 200, "Expected 200 status code for player update.");

        Allure.step("Step 4: Retrieve the updated player and check that all fields are updated");
        Response updatedPlayerResponse = playerSteps.get().getPlayerById(createdPlayer.getId());
        assertEquals(updatedPlayerResponse.getStatusCode(), 200, "Expected 200 status code for player update.");
        assertFalse(updatedPlayerResponse.getBody().asString().isEmpty(), "Response body is empty but expected player data.");
        PlayerModel updatedPlayerData = updatedPlayerResponse.as(PlayerModel.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(updatedPlayerData, updatedPlayer, "The updated player data does not match the expected updated data.");
        softAssert.assertEquals(updatedPlayerData.getId(), getPlayerResponse.getId(), "ID should not be null after update.");
        softAssert.assertAll();
    }

    @Test(description = "Verify that a player cannot be updated with invalid data")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test that a valid player cannot be updated with invalid data such as invalid age, role, password, and gender.")
    @Tag("negative")
    public void updatePlayerWithInvalidDataTest() {

        Allure.step("Step 1: Create a valid player");
        String suffix = String.valueOf(System.currentTimeMillis());
        PlayerModel validPlayer = PlayerModel.builder()
                .age(25)
                .gender(GENDER_MALE)
                .login("validLogin_" + suffix)
                .password("ValidPass123")
                .role(ADMIN)
                .screenName("validScreen_" + suffix)
                .build();

        PlayerModel createdPlayer = createValidPlayer(validPlayer, SUPERVISOR).as(PlayerModel.class);
        SoftAssert softAssert = new SoftAssert();

        Allure.step("Step 2: Try to update with invalid age (less than 16)");
        PlayerModel invalidAgePlayer = PlayerModel.builder()
                .age(15)  // Invalid age (too young)
                .gender(GENDER_MALE)
                .login(createdPlayer.getLogin())
                .password("ValidPass123")
                .role(ADMIN)
                .screenName(createdPlayer.getScreenName())
                .build();

        Response responseAge = playerSteps.get().updatePlayer(SUPERVISOR, createdPlayer.getId(), invalidAgePlayer);
        softAssert.assertEquals(responseAge.getStatusCode(), 400, "Expected error for invalid age.");

        Allure.step("Step 3: Try to update with invalid role (not 'supervisor' or 'admin')");
        PlayerModel invalidRolePlayer = PlayerModel.builder()
                .age(25)
                .gender(GENDER_MALE)
                .login(createdPlayer.getLogin())
                .password("ValidPass123")
                .role(ROLE_INVALID)  // Invalid role
                .screenName(createdPlayer.getScreenName())
                .build();

        Response responseRole = playerSteps.get().updatePlayer(SUPERVISOR, createdPlayer.getId(), invalidRolePlayer);
        softAssert.assertEquals(responseRole.getStatusCode(), 403, "Expected error for invalid role.");

        Allure.step("Step 4: Try to update with invalid gender (not 'male' or 'female')");
        PlayerModel invalidGenderPlayer = PlayerModel.builder()
                .age(25)
                .gender("other")  // Invalid gender
                .login(createdPlayer.getLogin())
                .password("ValidPass123")
                .role(ADMIN)
                .screenName(createdPlayer.getScreenName())
                .build();

        Response responseGender = playerSteps.get().updatePlayer(SUPERVISOR, createdPlayer.getId(), invalidGenderPlayer);
        softAssert.assertEquals(responseGender.getStatusCode(), 400, "Expected error for invalid gender.");

        Allure.step("Step 5: Try to update with invalid password (too short)");
        PlayerModel invalidPasswordPlayer = PlayerModel.builder()
                .age(25)
                .gender(GENDER_MALE)
                .login(createdPlayer.getLogin())
                .password("123")  // Invalid password (too short)
                .role(ADMIN)
                .screenName(createdPlayer.getScreenName())
                .build();

        Response responsePassword = playerSteps.get().updatePlayer(SUPERVISOR, createdPlayer.getId(), invalidPasswordPlayer);
        softAssert.assertEquals(responsePassword.getStatusCode(), 400, "Expected error for invalid password.");
        softAssert.assertAll();
    }
}
