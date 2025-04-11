package com.playercontroller.steps;

import com.playercontroller.api.PlayerApi;
import com.playercontroller.models.ApiResponse;
import com.playercontroller.models.PlayerModel;
import io.restassured.specification.RequestSpecification;

public class PlayerSteps {

    private final PlayerApi playerApi;

    public PlayerSteps(RequestSpecification spec) {
        this.playerApi = new PlayerApi(spec);
    }

    /**
     * Create a new player and return the player data as PlayerModel model
     * @param editor The login of the user performing the action
     * @param request The player creation request data
     * @return The player data as PlayerModel
     */
    public ApiResponse<PlayerModel> createPlayer(String editor, PlayerModel request) {
        return playerApi.createPlayer(editor, request);
    }
}
