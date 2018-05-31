package ru.relex.intertrust.set.client.views.gamefield;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface GameFieldResources extends ClientBundle {
    GameFieldResources INSTANCE = GWT.create(GameFieldResources.class);

    interface GameFieldStyles extends CssResource {
        @ClassName("game-field_exit")
        String gameField_exit();

        String statistic();

        String shape();

        @ClassName("disable")
        String disable();

        @ClassName("right-bar")
        String rightBar();

        String pass();

        @ClassName("left-bar_container")
        String leftBar_container();

        String active();

        String wrapper();

        String history();

        @ClassName("history-item")
        String historyItem();

        String separator();

        @ClassName("statistic-item")
        String statisticItem();

        String overflow();

        @ClassName("left-bar")
        String leftBar();

        String header();

        @ClassName("slide-button")
        String slideButton();

        String passed();

        String card();
    }

        @Source("GameField.gss")
        GameFieldStyles style();
}
