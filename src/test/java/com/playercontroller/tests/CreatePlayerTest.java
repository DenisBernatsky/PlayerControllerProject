package com.playercontroller.tests;

import com.playercontroller.api.PlayerApi;
import com.playercontroller.base.BaseTest;
import com.playercontroller.models.ApiResponse;
import com.playercontroller.models.ErrorResponse;
import com.playercontroller.models.PlayerModel;
import com.playercontroller.steps.PlayerSteps;
import io.qameta.allure.*;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;

@Epic("PlayerController")
@Feature("Create Player")
@Story("Basic creation")
@Tag("positive")
public class CreatePlayerTest extends BaseTest {

    @Test(description = "Create a valid user successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a supervisor can create a user with valid parameters.")
    public void createValidUserTest() {
        String suffix = String.valueOf(System.currentTimeMillis());

        // Create a request with valid data
        PlayerModel request = PlayerModel.builder()
                .age(25)
                .gender("male")
                .login("validUser_" + suffix)
                .password("Pass1234")
                .role("admin")
                .screenName("validScreen_" + suffix)
                .build();

        // Make the request and get the response
        ApiResponse<PlayerModel> apiResponse = new PlayerSteps(spec).createPlayer("supervisor", request);

        // Assert the response status code is 200 (successful creation)
        assertEquals(apiResponse.getStatusCode(), 200, "Expected successful user creation, but got error.");

        // Initialize soft assert for detailed field comparison
        SoftAssert softAssert = new SoftAssert();

        // Compare entire models (ignoring 'id' field)
        softAssert.assertEquals(apiResponse.getData(), request, "The created player data does not match the request data.");

        // Check that 'id' is not null in the response (should be generated after successful creation)
        softAssert.assertNotNull(apiResponse.getData().getId(), "ID should not be null after user creation.");

        // Call assertAll() to verify all soft assertions
        softAssert.assertAll();
    }

    @Test(description = "Verify that a user with an invalid role cannot be created")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a user with a role other than 'supervisor' or 'admin' cannot be created.")
    public void createUserWithInvalidRoleTest() {
        String suffix = String.valueOf(System.currentTimeMillis());

        // Create a request with an invalid role
        PlayerModel request = PlayerModel.builder()
                .age(25)
                .gender("male")
                .login("invalidRoleUser_" + suffix)
                .password("Pass1234")
                .role("invalidRole")  // Invalid role
                .screenName("invalidRoleScreen_" + suffix)
                .build();

        // Make the request and get response
        ApiResponse<PlayerModel> response = new PlayerSteps(spec).createPlayer("supervisor", request);

        // Assert that the status code is 400 (Bad Request) indicating an error for the invalid role
        assertEquals(response.getStatusCode(), 400, "Expected client error for invalid role.");
    }
}