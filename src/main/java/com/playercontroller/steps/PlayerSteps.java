package com.playercontroller.steps;

import com.playercontroller.api.PlayerApi;
import com.playercontroller.models.PlayerModel;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PlayerSteps {

    private final PlayerApi playerApi = new PlayerApi();

    /**
     * Create a new player and return the player data as PlayerModel model
     *
     * @param editor  The login of the user performing the action
     * @param request The player creation request data
     * @return The player data as PlayerModel
     */
    public Response createPlayer(String editor, PlayerModel request) {
        return playerApi.createPlayer(editor, request);
    }

    /**
     * Delete an existing player and return the result in ApiResponse
     *
     * @param editor   The login of the user performing the action
     * @param playerId The id of the player to delete
     * @return ApiResponse containing status and error messages if any
     */
    public Response deletePlayer(String editor, Long playerId) {
        return playerApi.deletePlayer(editor, playerId);
    }


    /**
     * Get player details by player ID.
     *
     * @param playerId The ID of the player to retrieve.
     * @return ApiResponse containing the player details.
     */
    public Response getPlayerById(Long playerId) {
        return playerApi.getPlayerById(playerId);
    }


    /**
     * Update an existing player's details.
     *
     * @param editor  The login of the user performing the action
     * @param id      The ID of the player to update
     * @param request The player update data
     * @return ApiResponse containing the updated player data or error messages
     */
    public Response updatePlayer(String editor, Long id, PlayerModel request) {
        return playerApi.updatePlayer(editor, id, request);
    }
}
