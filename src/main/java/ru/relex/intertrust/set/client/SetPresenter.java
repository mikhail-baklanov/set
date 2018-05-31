package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.client.service.SetServiceAsync;
import ru.relex.intertrust.set.client.uiHandlers.ObserveUIHandler;
import ru.relex.intertrust.set.client.uiHandlers.ExitGameUIHandler;
import ru.relex.intertrust.set.client.uiHandlers.GameFieldViewUIHandler;
import ru.relex.intertrust.set.client.uiHandlers.LoginViewUIHandler;
import ru.relex.intertrust.set.client.views.ApplyGameInfoView;
import ru.relex.intertrust.set.client.views.gameshort.GameShortView;
import ru.relex.intertrust.set.client.views.container.ContainerView;
import ru.relex.intertrust.set.client.views.gamefield.GameFieldView;
import ru.relex.intertrust.set.client.views.login.LoginView;
import ru.relex.intertrust.set.client.views.pregame.PreGameView;
import ru.relex.intertrust.set.client.views.result.ResultView;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameInfo;

import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

public class SetPresenter implements ExitGameUIHandler, LoginViewUIHandler, GameFieldViewUIHandler, ObserveUIHandler {

    /**
     * Период опроса сервера для получения значений таймера
     */
    private static final int REQUEST_PERIOD = 1000;
    /**
     * Создание экземпляра класса взаимодействия с сервисом
     */
    private static SetServiceAsync serviceAsync = GWT.create(SetService.class);
    private final ContainerView containerView;
    private GameShortView gameShortView;
    private ApplyGameInfoView anotherGameView;
    private LoginView loginView;
    private GameFieldView gameFieldView;
    private ResultView resultView;
    private PreGameView preGameView;
    /**
     * Текущий экран
     */
    private Widget currentView;
    /**
     * Аргумент в URL, устанавливающий номер игры.
     */
    private String gameRoom = com.google.gwt.user.client.Window.Location.getParameter("gameRoom");

    SetPresenter(ContainerView containerView) {
        this.containerView = containerView;

        loginView = new LoginView(this);
        gameFieldView = new GameFieldView(this);
        resultView = new ResultView(this);
        preGameView = new PreGameView(this);
        gameShortView = new GameShortView(this);

        anotherGameView = gameShortView;

        requestServer();

        Timer timer = new Timer() {
            @Override
            public void run() {
                requestServer();
            }
        };
        timer.scheduleRepeating(REQUEST_PERIOD);
    }

    @Override
    public void exit() {
        serviceAsync.exit(gameRoom, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {
                requestServer();

                if (currentView == gameFieldView)
                    gameFieldView.clearGameField();
            }
        });
    }

    /**
     * Запрос состояния игры с сервера
     */
    private void requestServer() {
        serviceAsync.getGameInfo(gameRoom, new AsyncCallback<GameInfo>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(GameInfo gameInfo) {
                processGameInfo(gameInfo);
            }
        });
    }


    /**
     * Начальный выбор view и настройка отображающейся на ней информации.
     * Если игроков нет, то отображается loginView.
     * Если игроки есть, то список игроков и время до начала игры.
     *
     * @param gameInfo информация о состоянии игры
     */
    private void processGameInfo(GameInfo gameInfo) {
        long gameTime = gameInfo.getTime();
        Widget newView;
        if (gameInfo.isStarted()) {
            if (gameInfo.isCurrentUserInGame()) {
                if (gameInfo.getDeckSize() == 0 && !gameInfo.isStarted()) {
                    resultView.setGameInfo(gameInfo);
                    newView = resultView;
                } else {
                    gameFieldView.setGameInfo(gameInfo);
                    newView = gameFieldView;
                }
            } else {
                if (gameInfo.isObserveMode()) {
                    gameFieldView.setGameInfo(gameInfo);
                    newView = gameFieldView;
                } else {
                    anotherGameView.setGameInfo(gameInfo);
                    newView = anotherGameView;
                }
            }
        } else {
            if (gameInfo.isPrepared() && gameInfo.isCurrentUserInGame()) {
                preGameView.setPreGameTimer(gameTime);
                preGameView.setPlayers(gameInfo.getPlayers());
                newView = preGameView;
//            } else if (gameInfo.getDeckSize() == 0) {
//                resultView.setGameInfo(gameInfo);
//                newView = resultView;
            } else {
                loginView.setLoginTimer(gameTime);
                newView = loginView;
            }
        }
        if (!newView.equals(currentView)) {
            currentView = newView;
            containerView.setView(currentView);
        }
    }

    @Override
    public void login(String name) {
        serviceAsync.login(name, gameRoom, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    requestServer();
                } else {
                    loginView.showLoginError();
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
            public void onSuccess(Void aVoid) {
                requestServer();
            }
        });
    }

    @Override
    public void observe() {
        serviceAsync.observe(gameRoom, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                consoleLog(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {
                requestServer();
            }
        });
    }

}
