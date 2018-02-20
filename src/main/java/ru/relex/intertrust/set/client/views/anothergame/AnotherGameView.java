package ru.relex.intertrust.set.client.views.anothergame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.uiHandlers.ChangeModeUIHandler;
import ru.relex.intertrust.set.client.constants.GameLocale;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.client.views.GameStateComposite;
import ru.relex.intertrust.set.shared.GameState;

import java.util.List;

public class AnotherGameView extends GameStateComposite {
    private GameLocale gameLocale = GWT.create(GameLocale.class);
    /**
     * Установка нового состояния игры
     * @param gameState новое состояние игры
     */
    @Override
    public void setGameState(GameState gameState) {
        setAnotherGameConstants();
        setAnotherGameTime(gameState.getTime());
        setAnotherGameCards(gameState.getDeck().size());
        setAnotherGamePlayers(gameState.getPlayers(), gameState.getScore());
    }

    interface AnotherGameViewUiBinder extends UiBinder<Widget, AnotherGameView> {
    }

    private static AnotherGameView.AnotherGameViewUiBinder uiBinder = GWT.create(AnotherGameView.AnotherGameViewUiBinder.class);
    /**
     * Время, прошедшее с начала игры
     */
    @UiField
    SpanElement anotherGameTime;

    /**
     * Количество оставшихся в колоде карт
     */
    @UiField
    SpanElement anotherGameCards;

    /**
     * Статистика каждого игрока
     */
    @UiField
    HTMLPanel anotherGamePlayers;

    @UiField
    DivElement alreadyGame;

    @UiField
    SpanElement gameTime;

    @UiField
    SpanElement cardsLeft;

    @UiField
    DivElement playerName;

    @UiField
    DivElement gamePoints;

    public void setAnotherGameConstants() {
        this.alreadyGame.setInnerHTML(gameLocale.alreadyGame());
        this.gameTime.setInnerHTML(gameLocale.gameTime());
        this.cardsLeft.setInnerHTML(gameLocale.cardsLeft());
        this.playerName.setInnerHTML(gameLocale.playerName());
        this.gamePoints.setInnerHTML(gameLocale.gamePoints());
    }

    /**
     * Обработчик смены режима прорисовки
     */
    private ChangeModeUIHandler uiHandler;
    public AnotherGameView(ChangeModeUIHandler uiHandler) {
        this.uiHandler = uiHandler;
        AnotherGameResources.INSTANCE.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Устанавливает время, прошеднее с начала игры
     * @param time время
     */
    private void setAnotherGameTime(Long time) {
        this.anotherGameTime.setInnerHTML(Utils.formatTime(time));
    }

    /**
     * Устанавливает количество оставшихся в колоде карт
     * @param cardsLeft количество карт
     */
    private void setAnotherGameCards(int cardsLeft) {
        this.anotherGameCards.setInnerHTML("" + cardsLeft);
    }

    /**
     * Устанавливает статистику для каждого игрока
     * @param players список игроков
     * @param score список результатов
     */
    private void setAnotherGamePlayers(List<String> players, List<Integer> score) {
        anotherGamePlayers.clear();
        for (int i = 0; i < players.size(); i++) {
            HTMLPanel widget = new HTMLPanel("<div class=\"game-started_players_item\">\n<div>" +
                    players.get(i) + "</div><div>" + score.get(i) + "</div>\n</div>");
            anotherGamePlayers.add(widget);
        }
    }

    @UiHandler("observe")
    public void onClick(ClickEvent e){
        uiHandler.changeMode();
    }
}

