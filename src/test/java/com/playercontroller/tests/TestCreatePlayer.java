package com.playercontroller.tests;

import com.playercontroller.models.PlayerModel;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.playercontroller.utils.TestConstants.*;
import static org.testng.Assert.assertEquals;

@Epic("PlayerController")
@Feature("Create Player")
public class TestCreatePlayer extends PlayerCommon {

    @Test(description = "Create a valid user successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a supervisor can create a user with valid parameters.")
    @Tag("positive")
    public void createValidUserTest() {

        Allure.step("Step 1: Generate valid user data");
        String suffix = String.valueOf(System.currentTimeMillis());
        String randomGender = List.of(GENDER_MALE, GENDER_FEMALE).get(new Random().nextInt(2));
        PlayerModel request = PlayerModel.builder()
                .age(ThreadLocalRandom.current().nextInt(17, 60))
                .gender(randomGender)
                .login(USER + ThreadLocalRandom.current().nextInt(1000, 9999))
                .password(RandomStringUtils.randomAlphanumeric(8, 15))
                .role(USER)
                .screenName("validScreen_" + suffix)
                .build();

        Allure.step("Step 2: Create the player with the generated data");
        String randomRole = List.of(SUPERVISOR, ADMIN).get(new Random().nextInt(2));
        Response response = playerSteps.get().createPlayer(randomRole, request);

        Allure.step("Step 3: Assert response status and validate created player data");
        assertEquals(response.getStatusCode(), 200, "Expected successful user creation, but got error with role: " + randomRole);
        PlayerModel responseModel = response.as(PlayerModel.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(responseModel, request, "The created player data does not match the request data.");
        softAssert.assertNotNull(responseModel.getId(), "ID should not be null after user creation.");
        softAssert.assertAll();
    }

    @Test(description = "Verify that a user with an invalid role cannot be created")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a user with a role other than 'supervisor' or 'admin' cannot be created.")
    @Tag("negative")
    public void createUserWithInvalidRoleTest() {

        Allure.step("Step 1: Create a request with an invalid role");
        String suffix = String.valueOf(System.currentTimeMillis());
        PlayerModel request = PlayerModel.builder()
                .age(25)
                .gender(GENDER_MALE)
                .login("invalidRoleUser_" + suffix)
                .password("Pass1234")
                .role(USER)
                .screenName("invalidRoleScreen_" + suffix)
                .build();

        Allure.step("Step 2: Make the request and validate response");
        Response response = playerSteps.get().createPlayer(USER, request);

        Allure.step("Step 3: Assert response status for invalid role");
        assertEquals(response.getStatusCode(), 403, "Expected client error for invalid role.");
    }
}
