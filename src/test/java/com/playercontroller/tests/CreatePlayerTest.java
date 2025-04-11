package com.playercontroller.tests;

import com.playercontroller.models.PlayerModel;
import com.playercontroller.steps.PlayerSteps;
import groovy.transform.Immutable;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.testng.Assert.*;

@Epic("PlayerController")
@Feature("Create Player")
public class CreatePlayerTest extends PlayerCommon {

    @Test(description = "Create a valid user successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a supervisor can create a user with valid parameters.")
    @Tag("positive")
    public void createValidUserTest() {

        // Create a request with valid data
        String suffix = String.valueOf(System.currentTimeMillis());
        String randomGender = List.of("male", "female").get(new Random().nextInt(2));
        PlayerModel request = PlayerModel.builder()
                .age(ThreadLocalRandom.current().nextInt(17, 60))
                .gender(randomGender)
                .login("user" + ThreadLocalRandom.current().nextInt(1000, 9999))
                .password(RandomStringUtils.randomAlphanumeric(8, 15))
                .role("user")
                .screenName("validScreen_" + suffix)
                .build();

        // Make the request and get the response
        String randomRole = List.of("supervisor", "admin").get(new Random().nextInt(2));
        Response response = playerSteps.get().createPlayer(randomRole, request);

        // Assert the response status code is 200 (successful creation)
        assertEquals(response.getStatusCode(), 200, "Expected successful user creation, but got error with role: " + randomRole);
        PlayerModel responseModel = response.as(PlayerModel.class);

        // Initialize soft assert for detailed field comparison
        SoftAssert softAssert = new SoftAssert();

        // Compare entire models (ignoring 'id' field)
        softAssert.assertEquals(responseModel, request, "The created player data does not match the request data.");

        // Check that 'id' is not null in the response (should be generated after successful creation)
        softAssert.assertNotNull(responseModel.getId(), "ID should not be null after user creation.");

        // Call assertAll() to verify all soft assertions
        softAssert.assertAll();
    }

    @Test(description = "Verify that a user with an invalid role cannot be created")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a user with a role other than 'supervisor' or 'admin' cannot be created.")
    @Tag("negative")
    public void createUserWithInvalidRoleTest() {
        String suffix = String.valueOf(System.currentTimeMillis());

        // Create a request with an invalid role
        PlayerModel request = PlayerModel.builder()
                .age(25)
                .gender("male")
                .login("invalidRoleUser_" + suffix)
                .password("Pass1234")
                .role("user")
                .screenName("invalidRoleScreen_" + suffix)
                .build();

        // Make the request and get response
        Response response = playerSteps.get().createPlayer("user", request);
        assertEquals(response.getStatusCode(), 403, "Expected client error for invalid role.");
    }
}