package ru.relex.intertrust.set.server;

import java.io.Serializable;

public class GameConnection implements Serializable {
    private String gameId;
    private String userName;
    private GameState gameState;
    private String observedGameId;

    public GameConnection(GameState gameState, String userName) {
        this.userName = userName;
        this.gameId = gameState.getGameId();
        this.gameState = gameState;
    }

    public String getObservedGameId() {
        return observedGameId;
    }

    public void setObservedGameId(String observedGameId) {
        this.observedGameId = observedGameId;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getGameId() {
        return gameId;
    }

    public String getUserName() {
        return userName;
    }

}
