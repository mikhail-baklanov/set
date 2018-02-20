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
import ru.relex.intertrust.set.client.constants.GameLocale;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.shared.GameState;

import java.util.List;

public class ResultView extends Composite {

    interface ResultViewUiBinder extends UiBinder<Widget, ResultView> {
    }

    private GameLocale gameLocale = GWT.create(GameLocale.class);

    /**
     * Установка нового состояния игры
     * @param gameState новое состояние игры
     */
    public final void setGameState(GameState gameState) {
        setResultGameTime(gameState.getTime());
        setResultSets(gameState.getCountSets());
        setResultGamePlayers(gameState.getPlayers(), gameState.getScore());
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
        gameResults.setInnerHTML(gameLocale.gameResults());
        gameTime.setInnerHTML(gameLocale.gameTime());
        setsCollected.setInnerHTML(gameLocale.setsCollected());
        playerName.setInnerHTML(gameLocale.playerName());
        gamePoints.setInnerHTML(gameLocale.gamePoints());
        exitGame.setHTML(gameLocale.exitGame());
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
     * @param score список результатов для каждого из игроков
     */
    private void setResultGamePlayers(List<String> players, List<Integer> score) {
        resultGamePlayers.clear();
        for (int i = 0; i < players.size(); i++) {
//            FlowPanel playerResultContainer = new FlowPanel();
//            playerResultContainer.setStyleName(style.gameStarted_players_item());
            HTMLPanel widget = new HTMLPanel("<div class=\"game-started_players_item\">\n<div>" +
                    players.get(i) + "</div><div>" + score.get(i) + "</div>\n</div>");
            resultGamePlayers.add(widget);
        }
    }
}
