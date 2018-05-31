package ru.relex.intertrust.set.client.views.gameshort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.uiHandlers.ObserveUIHandler;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.client.views.ApplyGameInfoView;
import ru.relex.intertrust.set.shared.GameInfo;
import ru.relex.intertrust.set.shared.Player;
import ru.relex.intertrust.set.shared.Players;

public class GameShortView extends ApplyGameInfoView {
    private static GameShortViewUiBinder uiBinder = GWT.create(GameShortViewUiBinder.class);
    /**
     * Время, прошедшее с начала игры
     */
    @UiField
    SpanElement gameTime;
    /**
     * Количество оставшихся в колоде карт
     */
    @UiField
    SpanElement deskSize;
    /**
     * Статистика каждого игрока
     */
    @UiField
    HTMLPanel players;

    @UiField
    Button observe;

    private ObserveUIHandler uiHandler;

    public GameShortView(ObserveUIHandler uiHandler) {
        this.uiHandler = uiHandler;
        GameShortResources.INSTANCE.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Обновление данных формы в соответствии с заданным состоянием игры.
     *
     * @param gameInfo состояние игры
     */
    @Override
    public void setGameInfo(GameInfo gameInfo) {
        gameTime.setInnerHTML(Utils.formatTime(gameInfo.getTime()));
        deskSize.setInnerHTML("" + gameInfo.getDeckSize());
        setPlayers(gameInfo.getPlayers());
    }

    /**
     * Устанавливает статистику для каждого игрока
     *
     * @param players список игроков
     */
    private void setPlayers(Players players) {
        this.players.clear();
        for (Player p : players.getPlayerList()) {
            HTMLPanel widget = new HTMLPanel("<div class=\"game-started_players_item\">\n<div>" +
                    p.getName() + "</div><div>" + p.getScore() + "</div>\n</div>");
            this.players.add(widget);
        }
    }

    @UiHandler("observe")
    public void onClick(ClickEvent e) {
        uiHandler.observe();
    }

    interface GameShortViewUiBinder extends UiBinder<Widget, GameShortView> {
    }
}

