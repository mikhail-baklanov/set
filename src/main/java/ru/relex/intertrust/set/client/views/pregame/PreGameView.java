package ru.relex.intertrust.set.client.views.pregame;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import ru.relex.intertrust.set.client.uiHandlers.ExitGameUIHandler;
import ru.relex.intertrust.set.client.l11n.GameStrings;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.shared.Player;
import ru.relex.intertrust.set.shared.Players;

public class PreGameView extends Composite {

    interface PreGameViewUiBinder extends UiBinder<Widget, PreGameView> {
    }
    private GameStrings gameStrings = GWT.create(GameStrings.class);
    private static PreGameViewUiBinder uiBinder = GWT.create(PreGameViewUiBinder.class);

    /**
     * Время, оставшееся до начала игры
     */
    @UiField
    SpanElement preGameTimer;

    /**
     * Контейнер для игроков, ожидающих начала игры
     */
    @UiField
    HTMLPanel playersContainer;

    /**
     * Контейнер для отображения таймера до начала игры
     */
    @UiField
    SpanElement beforeGame;
    /**
     * Контейнер для отображения номера игрока
     */
    @UiField
    DivElement number;
    /**
     * Контейнер для отображения имени игрока
     */
    @UiField
    DivElement namePlayer;
    /**
     * кнопка "выход"
     */
    @UiField
    Button exitGame;
    private ExitGameUIHandler exitListener;
    /**
     * Список игроков, ожидающих начало игры
     */
    private Players players = null;

    public PreGameView(ExitGameUIHandler exitListener) {
        this.exitListener = exitListener;
        PreGameResources.INSTANCE.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
        beforeGame.setInnerHTML(gameStrings.beforeGame());
        number.setInnerHTML(gameStrings.number());
        namePlayer.setInnerHTML(gameStrings.playerName());
        exitGame.setHTML(gameStrings.exitGame());
    }

    /**
     * Метод, обеспечивающий выход из ожидания начала игры
     * @param e событие нажатия мыши
     */
    @UiHandler("exitGame")
    public void onClick(ClickEvent e) {
        exitListener.exit();
    }

    /**
     * Вывод информации об оставшемся времени до начала игры
     * @param time время до начала игры в миллисекундах
     */
    public void setPreGameTimer(long time){
        preGameTimer.setInnerHTML(Utils.formatTime(time));
    }

    /**
     * Устанавливает список игроков, ожидающих начало игры
     * @param players список игроков
     */
    public void setPlayers (Players players) {
        if (!players.equals(this.players)) {
            this.players = players;
            playersContainer.clear();
            int i = 1;
            for (Player player: players.getPlayerList()) {
                HTMLPanel widget = new HTMLPanel("<div class=\"game-started_players_item\">\n<div>" +
                        (i++) + "</div><div>" + player.getName() + "</div>\n</div>");
                playersContainer.add(widget);
            }
        }
    }
}
