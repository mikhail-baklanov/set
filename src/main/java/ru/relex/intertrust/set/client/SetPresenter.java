package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import ru.relex.intertrust.set.client.uiHandlers.ChangeModeUIHandler;
import ru.relex.intertrust.set.client.uiHandlers.ExitGameUIHandler;
import ru.relex.intertrust.set.client.uiHandlers.GameFieldViewUIHandler;
import ru.relex.intertrust.set.client.uiHandlers.LoginViewUIHandler;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.client.service.SetServiceAsync;
import ru.relex.intertrust.set.client.views.GameStateComposite;
import ru.relex.intertrust.set.client.views.anothergame.AnotherGameView;
import ru.relex.intertrust.set.client.views.container.ContainerView;
import ru.relex.intertrust.set.client.views.gamefield.GameFieldView;
import ru.relex.intertrust.set.client.views.login.LoginView;
import ru.relex.intertrust.set.client.views.pregame.PreGameView;
import ru.relex.intertrust.set.client.views.result.ResultView;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import static ru.relex.intertrust.set.client.util.Utils.changeURL;
import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

public class SetPresenter implements ExitGameUIHandler, LoginViewUIHandler, GameFieldViewUIHandler, ChangeModeUIHandler {

    /**
     * Период опроса сервера для получения значений таймера
     */
    private static final int    REQUEST_PERIOD      =   1000;

    private final ContainerView containerView;
    private boolean             isAnotherGameView   =   true;
    private GameStateComposite  anotherGameView     =   new AnotherGameView(this);

    private String              playerName;
    private LoginView           loginView;
    private GameFieldView       gameFieldView;
    private ResultView          resultView;
    private PreGameView         preGameView;


    /**
     * Текущий экран
     */
    private Widget currentView;

    /**
     * Аргумент в URL, устанавливающий номер игры.
     */
    private String gameRoom = com.google.gwt.user.client.Window.Location.getParameter("gameRoom");


    /**
     * 	Создание экземпляра класса взаимодействия с сервисом
     */
    private static SetServiceAsync serviceAsync = GWT.create(SetService.class);

    @Override
    public void exit() {
        serviceAsync.exit(gameRoom, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {
                playerName = null;
                requestServer();

                if (currentView == gameFieldView)
                    gameFieldView.clearGameField();
            }
        });
    }

    SetPresenter(ContainerView containerView) {
        this.containerView = containerView;

        loginView = new LoginView(this);
        gameFieldView = new GameFieldView(this);
        resultView = new ResultView(this);
        preGameView = new PreGameView(this);

        requestServer();

        Timer timer = new Timer() {
           @Override
           public void run() {
               requestServer();
           }
       };
        timer.scheduleRepeating(REQUEST_PERIOD);
    }

    /**
     * Запрос состояния игры с сервера
     */
    private void requestServer () {
        serviceAsync.getGameState(gameRoom, new AsyncCallback<GameState>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(GameState gameState) {
                processGameState(gameState);
            }
        });
    }


    /**
     * Начальный выбор view и настройка отображающейся на ней информации.
     * Если игроков нет, то отображается loginView.
     * Если игроки есть, то список игроков и время до начала игры.
     *
     * @param gameState информация о состоянии игры
     */
    private void processGameState(GameState gameState) {
        long gameStateTime = gameState.getTime();
        Widget newView;
        if (gameState.isStart()) {
            if (gameStateTime >= 0) {
                if (hasCurrentPlayer(gameState)) {
                    gameFieldView.setGameState(gameState);
                    newView = gameFieldView;
                    if (gameState.getDeck().size() == 0 && !gameState.isStart()) {
                        resultView.setGameState(gameState);
                        newView = resultView;
                    }
                } else {
                    anotherGameView.setGameState(gameState);
                    newView = anotherGameView;
                }
            }
            else {
                newView = loginView;
            }
        } else {
            if (hasCurrentPlayer(gameState)) {
                    preGameView.setPreGameTimer(gameStateTime);
                    preGameView.setPlayers(gameState.getPlayers());
                    newView = preGameView;
            }
            else {
                if (gameStateTime < 0 && gameState.getActivePlayers() != 0)
                    loginView.setLoginTimer(gameStateTime);
                else
                    loginView.removeLoginTimer();
                newView = loginView;
            }
        }
        if (!newView.equals(currentView)) {
            currentView = newView;
            containerView.setView(currentView);
        }
    }

    /**
     * Проверяется можно ли использовать данное имя игроку.
     *
     * @param gameState информация о состоянии игры
     * @return true,если можно использовать имя
     *         false,если имя null или уже есть на сервере
     */
    private boolean hasCurrentPlayer(GameState gameState) {
        return playerName != null && gameState.hasPlayer(playerName);
    }

    @Override
    public void login(String name) {
        serviceAsync.login(name, gameRoom, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                if(result.equals("no"))
                    loginView.showLoginError();
                else if(result.equals("ok")) {
                    playerName = name;
                    currentView = preGameView;
                    containerView.setView(currentView);
                    requestServer();
                }
                else{
                    //если игрок уже логинился, то его перекидывает в комнату, в которой он логинился
                    //TODO возвращать игрока в то место, откуда он вышел
                    gameRoom=result;
                    playerName = name;
                    changeURL(result);
                    currentView = preGameView;
                    containerView.setView(currentView);
                    requestServer();
                }
            }
        });
    }

    @Override
    public void checkSet(Card[] cards) {
        serviceAsync.checkSet(cards, gameRoom, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                if (!result)
                    gameFieldView.showNotCorrectCards(cards);
                requestServer();
            }
        });
    }

    @Override
    public void pass(int count) {
        serviceAsync.pass(count, gameRoom, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) { requestServer(); }
        });
    }

    @Override
    public void changeMode() {
        if (isAnotherGameView){
            anotherGameView = new GameFieldView(this);
        } else
            anotherGameView = new AnotherGameView(this);
        isAnotherGameView = !isAnotherGameView;
    }

    @Override
    public boolean canChange(GameState gameState) {
        //Если текущего игрока нет на сервере, то можно менять режимы отображения
        return !hasCurrentPlayer(gameState);
    }
}