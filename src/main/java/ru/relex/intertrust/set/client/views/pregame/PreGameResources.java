package ru.relex.intertrust.set.client.views.pregame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface PreGameResources extends ClientBundle {
    PreGameResources INSTANCE = GWT.create(PreGameResources.class);

    interface PreGameStyles extends CssResource {
        @ClassName("pregame-block_exit")
        String loginBlock_exit();

        @ClassName("pregame-block_hello")
        String loginBlock_hello();

        @ClassName("game-started_players_item")
        String gameStarted_players_item();

        @ClassName("game-started_players_items")
        String gameStarted_players_items();

        @ClassName("pregame-block_waiting-game")
        String loginBlock_waitingGame();
    }

        @Source("PreGame.gss")
        PreGameStyles style();
}
