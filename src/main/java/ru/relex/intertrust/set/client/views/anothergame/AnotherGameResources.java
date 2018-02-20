package ru.relex.intertrust.set.client.views.anothergame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface AnotherGameResources extends ClientBundle {
    AnotherGameResources INSTANCE = GWT.create(AnotherGameResources.class);

    interface AnotherGameStyles extends CssResource {
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

        @ClassName("change_mode")
        String change_mode();
    }

        @Source("AnotherGame.gss")
        AnotherGameStyles style();
}
