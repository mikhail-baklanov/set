package ru.relex.intertrust.set.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameInfo;

import javax.servlet.ServletException;
import java.util.*;

import static ru.relex.intertrust.set.server.GameStateConstants.TICK_MS;

/**
 * Класс, содержащий серверную логику игры Set.
 */
public class SetServiceImpl extends RemoteServiceServlet implements SetService {

    // Ключ для хранения состояний игр в Application Context сервлета.
    // Значением является Map<String, GameState>, где String - имя игры ("комнаты"), GameState - состояние игры
    private static final String APP_CONTEXT_GAMES = "games";
    private static final String SESSION_CONTEXT_GAMES_CONNECTIONS = "connections";

    private TimerTask timerTask = new Ticker();
    private Timer timer = new Timer();

    /**
     * Первоначальная инициализация.
     * Выполняется при первом запуске сервера.
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        super.init();
        initGame();
        timer.schedule(timerTask, 0, TICK_MS);
    }

    @Override
    public void destroy() {
        timer.cancel();
        super.destroy();
    }

    /**
     * Инициализация игры.
     */
    private void initGame() {
        getServletContext().setAttribute(APP_CONTEXT_GAMES,
                Collections.synchronizedMap(new HashMap</*gameRoom*/String, GameState>()));
    }

    private GameState getGameState(String gameRoom) {
        Map<String, GameState> map = (Map<String, GameState>) getServletContext().getAttribute(APP_CONTEXT_GAMES);
        if (!map.containsKey(gameRoom))
            map.put(gameRoom, new GameState());
        return map.get(gameRoom);
    }

    @Override
    public GameInfo getGameInfo(String gameRoom) {
        GameState gameState = getGameState(gameRoom);
        GameConnection gc = null;

        Map<GameState, GameConnection> connections = (Map<GameState, GameConnection>) getThreadLocalRequest().getSession().getAttribute(SESSION_CONTEXT_GAMES_CONNECTIONS);
        if (connections != null)
            gc = connections.get(gameState);

        if (gc == null) {
            return getGameState(gameRoom).getGameInfo("", false);
        } else {
            String userName = gameState.getGameId().equals(gc.getGameId()) ? gc.getUserName() : "";
            return getGameState(gameRoom).getGameInfo(userName, gameState.getGameId().equals(gc.getObservedGameId()));
        }
    }

    @Override
    public boolean login(String name, String gameRoom) {
        GameState gameState = getGameState(gameRoom);
        synchronized (gameState) {

            Map<GameState, GameConnection> connections = (Map<GameState, GameConnection>) getThreadLocalRequest().getSession().getAttribute(SESSION_CONTEXT_GAMES_CONNECTIONS);
            if (connections == null) {
                connections = new HashMap<>();
                getThreadLocalRequest().getSession().setAttribute(SESSION_CONTEXT_GAMES_CONNECTIONS, connections);
            }

            GameConnection gameConnection = connections.get(gameState);
            if (!gameState.isStoped() &&
                    gameConnection != null &&
                    gameState.getGameId().equals(gameConnection.getGameId()) &&
                    gameConnection.getUserName().equals(name))
                return true;

            boolean result = gameState.loginPlayer(name);
            if (result) {
                gameConnection = new GameConnection(gameState, name);
                connections.put(gameState, gameConnection);
            }
            return result;
        }
    }

    private GameConnection getUserGame(String gameRoom) {
        Map<GameState, GameConnection> connections = (Map<GameState, GameConnection>) getThreadLocalRequest().getSession().getAttribute(SESSION_CONTEXT_GAMES_CONNECTIONS);
        if (connections == null)
            return null;
        GameState gameState = getGameState(gameRoom);

        GameConnection gameConnection = connections.get(gameState);
        if (gameConnection == null)
            return null;
        if (!gameState.getGameId().equals(gameConnection.getGameId()))
            return null;
        return gameConnection;
    }

    @Override
    public void pass(int cardsInDeck, String gameRoom) {
        GameConnection gc = getUserGame(gameRoom);
        if (gc == null)
            return;
        synchronized (gc.getGameState()) {
            if (gc.getGameState().getDeckSize() == cardsInDeck)
                gc.getGameState().pass(gc.getUserName());
        }
    }

    @Override
    public boolean checkSet(Card[] cards, String gameRoom) {
        GameConnection gc = getUserGame(gameRoom);
        if (gc == null)
            return false;
        boolean isSet;
        synchronized (gc.getGameState()) {
            isSet = gc.getGameState().checkSet(cards, gc.getUserName());
        }
        return isSet;
    }

    @Override
    public void exit(String gameRoom) {
        GameConnection gc = null;

        GameState gameState = getGameState(gameRoom);
        Map<GameState, GameConnection> connections = (Map<GameState, GameConnection>) getThreadLocalRequest().getSession().getAttribute(SESSION_CONTEXT_GAMES_CONNECTIONS);
        if (connections != null)
        gc = connections.get(gameState);
        if (gc != null) {
            if (gameState.getGameId().equals(gc.getObservedGameId())) {
                gc.setObservedGameId(null);
                return;
            }
            if (!gameState.getGameId().equals(gc.getGameId()))
                return;
            synchronized (gc.getGameState()) {
                gc.getGameState().exitPlayer(gc.getUserName());
            }
        }
    }

    @Override
    public void observe(String gameRoom) {
        GameState gameState = getGameState(gameRoom);

        Map<GameState, GameConnection> connections = (Map<GameState, GameConnection>) getThreadLocalRequest().getSession().getAttribute(SESSION_CONTEXT_GAMES_CONNECTIONS);
        if (connections == null) {
            connections = new HashMap<>();
            getThreadLocalRequest().getSession().setAttribute(SESSION_CONTEXT_GAMES_CONNECTIONS, connections);
        }
        GameConnection gc = connections.get(gameState);
        if (gc == null) {
            gc = new GameConnection(gameState, "");
            connections.put(gameState, gc);
        }
        if (gameState.getGameId().equals(gc.getObservedGameId()))
            return;
        synchronized (gc.getGameState()) {
            gc.setObservedGameId(gameState.getGameId());
        }
    }

    /**
     * Таймер, каждые TICK_MS миллисекунд обновляющий игровое время во всех играх.
     */
    private class Ticker extends TimerTask {
        /**
         * Обновление времени в играх.
         */
        @Override
        public void run() {
            for (GameState gameState :
                    ((Map<String, GameState>) getServletContext().getAttribute(APP_CONTEXT_GAMES)).values()) {
                synchronized (gameState) {
                    gameState.tick(TICK_MS);
                }
            }
        }
    }
}
