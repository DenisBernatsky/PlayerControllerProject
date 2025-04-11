package com.playercontroller.api;

import com.playercontroller.models.PlayerModel;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.concurrent.TimeUnit;

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
     *
     * @param editor  Login of the user performing the operation
     * @param request Request body with new player data
     * @return ApiResponse with player data and error messages if any
     */
    public Response createPlayer(String editor, PlayerModel request) {
        // Send a GET request with parameters
        return given()
                .spec(spec)
                .param("age", request.getAge())
                .param("gender", request.getGender())
                .param("login", request.getLogin())
                .param("password", request.getPassword())
                .param("role", request.getRole())
                .param("screenName", request.getScreenName())
                .get("/player/create/{editor}", editor);

    }

    /**
     * Delete an existing player.
     * Method: DELETE
     * URL: /player/delete/{editor}
     *
     * @param editor   The login of the user performing the operation (supervisor/admin)
     * @param playerId The id of the player to be deleted
     * @return ApiResponse containing the result of the operation
     */
    public Response deletePlayer(String editor, Long playerId) {
        // Send a DELETE request with playerId from the request body
        return given()
                .spec(spec)
                .pathParam("editor", editor)  // Set the editor login (user performing the operation)
                .body("{\"playerId\": " + playerId + "}")  // Set the request body with playerId
                .delete("/player/delete/{editor}");
    }

    /**
     * Get player details by player ID.
     * Method: POST
     * URL: /player/get
     *
     * @param playerId The ID of the player to fetch details for
     * @return ApiResponse with player data if successful, or error messages if any
     */
    public Response getPlayerById(Long playerId) {
        int attempts = 0;
        Response response = null;

        while (attempts < 3) {
            response = given()
                    .spec(spec)
                    .body("{\"playerId\": " + playerId + "}")
                    .post("/player/get");

            if (response.getStatusCode() == 200 && !response.getBody().asString().isEmpty()) {
                break;
            }

            attempts++;
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return response;
    }

    /**
     * Update player details.
     * Method: PATCH
     * URL: /player/update/{editor}/{id}
     *
     * @param editor  Login of the user performing the operation (supervisor/admin)
     * @param id      The ID of the player to update
     * @param request The player update data
     * @return ApiResponse with updated player data if successful, or error messages if any
     */
    public Response updatePlayer(String editor, Long id, PlayerModel request) {
        // Send PATCH request with playerId in the path and update data in the body
        return given()
                .spec(spec)
                .pathParam("editor", editor)
                .pathParam("id", id)
                .body(request)
                .patch("/player/update/{editor}/{id}");
    }
}