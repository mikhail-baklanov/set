package ru.relex.intertrust.set.client.views.gameshort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface GameShortResources extends ClientBundle {
    GameShortResources INSTANCE = GWT.create(GameShortResources.class);

    interface GameShortStyles extends CssResource {
        @ClassName("game-started_item")
        String gameStarted_item();

        @ClassName("login-block_hello")
        String loginBlock_hello();

        @ClassName("game-started_players_item")
        String gameStarted_players_item();

        @ClassName("game-started_players_items")
        String gameStarted_players_items();

        @ClassName("login-block_game-started")
        String loginBlock_gameStarted();

        @ClassName("game-started_observe")
        String observe();
    }

        @Source("GameShort.gss")
        GameShortStyles style();
}
