package com.playercontroller.api;

import com.playercontroller.models.PlayerModel;
import com.playercontroller.utils.Specifications;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PlayerApi {

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
        return given()
                .spec(Specifications.getSpec())
                .pathParam("editor", editor)
                .param("age", request.getAge())
                .param("gender", request.getGender())
                .param("login", request.getLogin())
                .param("password", request.getPassword())
                .param("role", request.getRole())
                .param("screenName", request.getScreenName())
                .get("/player/create/{editor}");
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
        return given()
                .spec(Specifications.getSpec())
                .pathParam("editor", editor)
                .body("{\"playerId\": " + playerId + "}")
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
        return given()
                .spec(Specifications.getSpec())
                .body("{\"playerId\": " + playerId + "}")
                .post("/player/get");
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
        return given()
                .spec(Specifications.getSpec())
                .pathParam("editor", editor)
                .pathParam("id", id)
                .body(request)
                .patch("/player/update/{editor}/{id}");
    }
}
