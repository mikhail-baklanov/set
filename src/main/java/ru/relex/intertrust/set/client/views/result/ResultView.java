package ru.relex.intertrust.set.client.views.result;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.uiHandlers.ExitGameUIHandler;
import ru.relex.intertrust.set.client.l11n.GameStrings;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.shared.GameInfo;
import ru.relex.intertrust.set.shared.Player;
import ru.relex.intertrust.set.shared.Players;

public class ResultView extends Composite {

    interface ResultViewUiBinder extends UiBinder<Widget, ResultView> {
    }

    private GameStrings gameStrings = GWT.create(GameStrings.class);

    /**
     * Установка нового состояния игры
     * @param gameInfo новое состояние игры
     */
    public final void setGameInfo(GameInfo gameInfo) {
        setResultGameTime(gameInfo.getTime());
        setResultSets(gameInfo.getSetsCount());
        setResultGamePlayers(gameInfo.getPlayers());
    }

    private static ResultViewUiBinder uiBinder = GWT.create(ResultViewUiBinder.class);

    /**
     * Время игры
     */
    @UiField
    SpanElement resultGameTime;

    /**
     * Количество собранных сетов
     */
    @UiField
    SpanElement resultSets;

    /**
     * Контейнер с результатами игроков
     */
    @UiField
    HTMLPanel resultGamePlayers;

    /**
     * Обработчик события выхода из окна результатов.
     */
    private ExitGameUIHandler exitListener;

    /**
     * Заголовок окна результатов.
     */
    @UiField
    DivElement gameResults;

    /**
     * Заголовок для времени игры.
     */
    @UiField
    SpanElement gameTime;

    /**
     * Заголовок для собранных сетов.
     */
    @UiField
    SpanElement setsCollected;

    /**
     * Заголовок для колонки игроков.
     */
    @UiField
    DivElement playerName;

    /**
     * Заголовок для колонки очков.
     */
    @UiField
    DivElement gamePoints;

    /**
     * Заголовок для кнопки выход.
     */
    @UiField
    Button exitGame;

    /**
     *  Пременная для доступа к стилям.
     */
    private static ResultResources.ResultStyles style = ResultResources.INSTANCE.style();


    /**
     *  Метод, который заполняет View статичным текстом.
     */
    public ResultView(ExitGameUIHandler exitListener) {
        this.exitListener = exitListener;
        ResultResources.INSTANCE.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
        gameResults.setInnerHTML(gameStrings.gameResults());
        gameTime.setInnerHTML(gameStrings.gameTime());
        setsCollected.setInnerHTML(gameStrings.setsCollected());
        playerName.setInnerHTML(gameStrings.playerName());
        gamePoints.setInnerHTML(gameStrings.gamePoints());
        exitGame.setHTML(gameStrings.exitGame());
    }

    /**
     * Метод, обеспечивающий выход из окна результатов.
     * @param e событие нажатия мыши
     */
    @UiHandler("exitGame")
    public void onClick(ClickEvent e) {
        exitListener.exit();
    }

    public void setResultGameTime(Long time) {
        resultGameTime.setInnerHTML(Utils.formatTime(time));
    }

    /**
     * Устанавливает количество собранных сетов
     * @param numberOfSets количество сетов
     */
    private void setResultSets(int numberOfSets) {
        this.resultSets.setInnerHTML("" + numberOfSets);
    }

    /**
     * Устанавливает результаты
     * @param players список игроков
     */
    private void setResultGamePlayers(Players players) {
        resultGamePlayers.clear();
        for (Player p: players.getPlayerList()) {
//            FlowPanel playerResultContainer = new FlowPanel();
//            playerResultContainer.setStyleName(style.gameStarted_players_item());
            HTMLPanel widget = new HTMLPanel("<div class=\"game-started_players_item\">\n<div>" +
                    p.getName() + "</div><div>" + p.getScore() + "</div>\n</div>");
            resultGamePlayers.add(widget);
        }
    }
}
