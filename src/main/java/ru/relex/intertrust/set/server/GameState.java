package ru.relex.intertrust.set.server;

import ru.relex.intertrust.set.shared.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.relex.intertrust.set.server.GameStateConstants.*;

public class GameState {

    private Deck deck = new Deck();                     // колода
    private List<Card> cardsOnDesk = new ArrayList<>(); // карты на столе
    private Players players = new Players();
    private FinishCause finishCause;

    // синтетическое время
    private long time = 0;
    // момент начала игры по синтетическому времени. Если равен INACTIVE_GAME_TIME, то игра не активирована
    private long startTime = INACTIVE_GAME_TIME;
    // момент последней активности одного из игроков по синтетическому времени
    private long lastActivityTime;

    public GameState() {
    }


    private void finishGame(FinishCause finishCause) {
        this.finishCause = finishCause;
        startTime = INACTIVE_GAME_TIME;
    }

    private void prepareGame() {
        finishCause = null;
        startTime = time + TIME_TO_GAME_MS;
        lastActivityTime = startTime;
        deck.reset();
        players.reset();
        cardsOnDesk.clear();
        addCardsOnDesk(INITIAL_NUMBER_OF_CARDS);
    }

    public boolean hasPlayer(String name) {
        return players.hasPlayer(name);
    }

    private void registerActivity() {
        if (startTime != INACTIVE_GAME_TIME)
            lastActivityTime = time;
    }

    public boolean isStarted() {
        return startTime != INACTIVE_GAME_TIME && time >= startTime;
    }

    public boolean isPrepared() {
        return startTime != INACTIVE_GAME_TIME && time < startTime;
    }

    public boolean isStoped() {
        return startTime == INACTIVE_GAME_TIME;
    }

    public void resetGame() {
        startTime = INACTIVE_GAME_TIME;
        lastActivityTime = 0;
    }

    public long getGameTime() {
        return startTime == INACTIVE_GAME_TIME ? 0 : time - startTime;
    }

    public int getDeckSize() {
        return deck.size();
    }

    public Collection<Card> getCardsOnDesk() {
        return cardsOnDesk;
    }

    public Players getPlayers() {
        return players;
    }

    /**
     * Добавление нового игрока.
     *
     * @param name имя игрока
     */
    public boolean loginPlayer(String name) {
        if (isStoped()) {
            prepareGame();
            players.addPlayer(name);
            return true;
        } else if (isPrepared() && !hasPlayer(name)) {
            players.addPlayer(name);
            return true;
        }
        return false;
    }

    /**
     * Добавление нескольких карт на стол из колоды.
     *
     * @param count количество добавляемых карт
     */
    private void addCardsOnDesk(int count) {
        for (int i = 0; i < count; i++) {
            getCardsOnDesk().add(deck.pop());
        }
    }

    /**
     * Удаление игрока из списка игроков.
     * Удаление всех данных, если игра не начата.
     * Удаление с сохранением ника и счета, если игра идет.
     *
     * @param name
     */
    public void exitPlayer(String name) {
        if (isPrepared()) {
            players.removePlayer(name);
            if (players.getActivePlayersCount() == 0)
                finishGame(FinishCause.ALL_PLAYERS_EXIT_BEFORE_GAME);
        } else if (isStarted()) {
            players.setInactive(name);
            if (players.getActivePlayersCount() == 0)
                finishGame(FinishCause.ALL_PLAYERS_EXIT_DUE_GAME);
        }
    }

    /**
     * Метод, осуществляющий пас.
     * Проверка количества спасовавших.
     * Добавление карт на стол или завершение игры, если карт в колоде нет.
     *
     * @param name имя игрока
     */
    public void pass(String name) {
        registerActivity();

        Player p = getPlayers().getPlayerByName(name);
        if (p == null)
            return;
        p.setPassed(true);

        if (manyPass()) {
            players.resetPassed();
            if (deck.empty()) {
                finishGame(FinishCause.MANY_PLAYERS_PASS_WITH_EMPTY_DECK);
            } else if (getCardsOnDesk().size() < MAX_NUMBER_OF_CARDS)
                addCardsOnDesk(SET_SIZE);
        }
    }

    private boolean manyPass() {
        if (players.getActivePlayersCount() == 2)
            return players.getPassedPlayersCount() == 2;
        return players.getPassedPlayersCount() >= (players.getActivePlayersCount() + 1) / 2;
    }

    /**
     * Проверка, являются ли выбранные карты сетом.
     * Если нет, то у клиента вычитаются очки.
     * Если являются, то проверяется, есть ли в текущей игре на столе данные карты.
     * Если есть, клиенту добавляются очки, а со стола удаляются данные карты и запускается проверка, остались ли карты в колоде.
     * Если остались, на стол добавляются 3 новые карты из колоды.
     * Если в колоде не осталось карт, то проверяется, есть ли карты на столе, если нет - игра заканчивается (isStarted становится false).
     *
     * @param set  набор карт, выбранных игроком
     * @param name имя игрока
     * @return true, если это сет
     */
    public boolean checkSet(Card[] set, String name) {
        registerActivity();

//        int[] sum = {3, 3, 3, 3};
        int[] sum = {0, 0, 0, 0};
        for (int i = 0; i < SET_SIZE; i++) {
            sum[0] += set[i].getColor();
            sum[1] += set[i].getShapeCount();
            sum[2] += set[i].getFill();
            sum[3] += set[i].getShape();
        }

        for (int i = 0; i <= 3; i++) {
            if (!(sum[i] == SET_SIZE || sum[i] == SET_SIZE * 2 || sum[i] == SET_SIZE * 3)) {
                players.getPlayerByName(name).addScore(PENALTY);
                return false;
            }
        }
        if (!players.getPlayerByName(name).isPassed()) {
            int existSet = 0;
            for (Card c : set) {
                if (cardsOnDesk.contains(c))
                    existSet++;
            }
            if (existSet != SET_SIZE)
                return false;
            else {
                players.getPlayerByName(name).addScore(REWARD);
                for (Card c : set)
                    getCardsOnDesk().remove(c);

                if (!deck.empty() && getCardsOnDesk().size() < INITIAL_NUMBER_OF_CARDS) {
                    addCardsOnDesk(SET_SIZE);
                }
                if (getCardsOnDesk().size() == 0)
                    finishGame(FinishCause.ALL_SETS_FOUND);
            }
        }
        return true;
    }


    public void tick(int ms) {
        if (!isStoped()) {
            time += ms;
            if (isStarted() && (time - lastActivityTime > INACTIVITY_PERIOD_MS)) {
                finishGame(FinishCause.INACTIVITY);
            }
        }
    }

    public String getGameId() {
        return startTime == INACTIVE_GAME_TIME ? "" : "" + startTime;
    }

    public GameInfo getGameInfo(String currentUserName, boolean isObserved) {
        GameInfo gameInfo = new GameInfo();
        gameInfo.setDeck(deck);
        gameInfo.setCardsOnDesk(cardsOnDesk);
        gameInfo.setPlayers(players);
        gameInfo.setTime(getGameTime());
        gameInfo.setCurrentUserName(currentUserName);
        gameInfo.setStarted(isStarted());
        gameInfo.setPrepared(isPrepared());
        gameInfo.setFinishCause(finishCause);
        gameInfo.setObserveMode(isObserved);
        return gameInfo;
    }
}
