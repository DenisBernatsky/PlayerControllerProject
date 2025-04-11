package com.playercontroller.api;

import com.playercontroller.models.ApiResponse;
import com.playercontroller.models.ErrorResponse;
import com.playercontroller.models.PlayerModel;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.RestAssured.given;

public class PlayerApi {

    private final RequestSpecification spec;

    public PlayerApi(RequestSpecification spec) {
        this.spec = spec;
    }

    /**
     * Create a new player.
     * Method: GET
     * URL: /player/create/{editor}
     * @param editor  Login of the user performing the operation
     * @param request Request body with new player data
     * @return ApiResponse with player data and error messages if any
     */
    public ApiResponse<PlayerModel> createPlayer(String editor, PlayerModel request) {
        // Send a GET request with parameters
        Response response = given()
                .spec(spec)
                .param("age", request.getAge())
                .param("gender", request.getGender())
                .param("login", request.getLogin())
                .param("password", request.getPassword())
                .param("role", request.getRole())
                .param("screenName", request.getScreenName())
                .get("/player/create/{editor}", editor);

        // If the status code is in the successful range (2xx)
        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            // Successful response - deserialize to PlayerModel
            return new ApiResponse<>(response.as(PlayerModel.class), response.getStatusCode(), null);
        }

        // If the response body is empty, check the status code and the error
        if (response.getBody().asString().isEmpty()) {
            // Return an error with empty content
            ErrorResponse errorResponse = new ErrorResponse("No content", response.getStatusCode(), "Error", "", "");
            return new ApiResponse<>(null, response.getStatusCode(), List.of(errorResponse));
        }

        // Deserialize the error response
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        return new ApiResponse<>(null, response.getStatusCode(), List.of(errorResponse));
    }
}
