package ru.relex.intertrust.set.client.views.result;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ResultResources extends ClientBundle {
    ResultResources INSTANCE = GWT.create(ResultResources.class);

    interface ResultStyles extends CssResource {
        @ClassName("game-started_item")
        String gameStarted_item();

        @ClassName("login-block_exit")
        String loginBlock_exit();

        @ClassName("login-block_hello")
        String loginBlock_hello();

        @ClassName("game-started_players_item")
        String gameStarted_players_item();

        @ClassName("game-started_players_items")
        String gameStarted_players_items();

        @ClassName("login-block_game-started")
        String loginBlock_gameStarted();
    }

        @Source("Result.gss")
        ResultStyles style();
}
