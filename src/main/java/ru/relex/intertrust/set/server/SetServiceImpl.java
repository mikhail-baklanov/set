package ru.relex.intertrust.set.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.relex.intertrust.set.client.service.SetService;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static ru.relex.intertrust.set.shared.GameState.INACTIVITY_TIME;
import static ru.relex.intertrust.set.shared.GameState.getPlayersRoom;

/**
 * Класс, содержащий серверную логику игры Set.
 */
public class SetServiceImpl extends RemoteServiceServlet implements SetService {

    private TimerTask t = new StartTimer();
    private Timer timer = new Timer();
    private static final String GAME_STATE = "gameState";
    private static final String USER_NAME = "userName";
    private static final int INITIAL_NUMBER_OF_CARDS = 12;
    public static final long PERIOD_MS = 1000;


    @Override
    public GameState getGameState(String gameRoom) {
        HashMap<String, GameState> map = (HashMap<String, GameState>) getServletContext().getAttribute(GAME_STATE);
        if (!map.containsKey(gameRoom))
            map.put(gameRoom, new GameState());
        GameState gameState = map.get(gameRoom);
            return gameState;
    }

    public void newGameState(String gameRoom) {
        HashMap<String, GameState> map = (HashMap<String, GameState>) getServletContext().getAttribute(GAME_STATE);
            map.put(gameRoom, new GameState());
    }

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
        timer.schedule(t, 0, PERIOD_MS);
    }

    /**
     * Инициализация игры.
     */
    public void initGame() {
        getServletContext().setAttribute(GAME_STATE, new HashMap<String, GameState>());
    }

    @Override
    public String login(String name, String id) {
        //если сессия уже есть, то возвращаем клиенту номер комнаты, в которую мы изначально заходили
        if(getThreadLocalRequest().getSession().getAttribute(USER_NAME)!=null){
            name=(String)getThreadLocalRequest().getSession().getAttribute(USER_NAME);
            GameState gameState = getGameState(getPlayersRoom(name));
            synchronized (gameState) {
                //if (!gameState.hasPlayer(name) && !gameState.isStart())
                //gameState.createNewPlayer(name);
                return getPlayersRoom(name);
            }
        }
        if (name.trim().isEmpty())
            return "no";
        GameState gameState = getGameState(id);

        boolean success;
        synchronized (gameState) {
            success = !gameState.hasPlayer(name) && !gameState.isStart() &&
                    getThreadLocalRequest().getSession().getAttribute(USER_NAME) == null;
            if (success) {
                gameState.createNewPlayer(name);
                getThreadLocalRequest().getSession().setAttribute(USER_NAME, name);
                gameState.addRoom(id,name);
                return "ok";
            }
            return "no";
        }
    }

    /**
     * Метод, вызывающий инициализацию игрового процесса.
     */
    public void startGame(String id) {
        GameState gameState = getGameState(id);
        gameState.startGame(INITIAL_NUMBER_OF_CARDS);

    }

    @Override
    public void exit(String id) {
        GameState gameState = getGameState(id);
        synchronized (gameState) {
            gameState.removePlayer((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
            getThreadLocalRequest().getSession().removeAttribute(USER_NAME);
            if (gameState.getActivePlayers() == 0) {
                //reloadTimer();
                newGameState(id);
            }
        }
    }

    @Override
    public void pass(int cardsInDeck, String id) {
        //TODO fix pass
        GameState gameState = getGameState(id);
        synchronized (gameState) {
            String user = (String) getThreadLocalRequest().getSession().getAttribute(USER_NAME);
            gameState.AddNotAbleToPlay(user, cardsInDeck);
            gameState.pass(user);
            gameState.setInactivityTime(-INACTIVITY_TIME);
        }
    }

    @Override
    public boolean checkSet(Card[] set, String id) {
        GameState gameState = getGameState(id);
        boolean isSet;
        synchronized (gameState) {
            String player = (String) getThreadLocalRequest().getSession().getAttribute(USER_NAME);
            isSet = gameState.checkSet(set, player);
            gameState.setInactivityTime(-INACTIVITY_TIME);
        }
        return isSet;
    }

    public boolean isPassed(String id) {
        GameState gameState = getGameState(id);
        return gameState.isPassed((String) getThreadLocalRequest().getSession().getAttribute(USER_NAME));
    }

    /**
     * Метод обнуляет таймер.
     */
    private void reloadTimer() {
        timer.cancel();
        timer = new Timer();
        t = new StartTimer();
    }

    /**
     * Класс, переодически обновляющий игровое время каждые PERIOD_MS миллисекунд.
     * Если время равняется 0 миллисекунд, начинает игру.
     */
    private class StartTimer extends TimerTask {
        /**
         * Метод, осуществляющий обновление времени.
         */
        @Override
        public void run() {
            for (Map.Entry<String, GameState> pair :
                    ((HashMap<String, GameState>) getServletContext().getAttribute(GAME_STATE)).entrySet()) {
                GameState gameState = getGameState(pair.getKey());
                if (gameState.getPlayers().size() != 0)
                    synchronized (gameState) {
                        gameState.tick();
                        if (gameState.getTime() == 0) startGame(pair.getKey());
                        if (gameState.getInactivityTime() == 0) newGameState(pair.getKey());

                    }
            }
        }
    }



}
