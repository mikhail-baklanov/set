package ru.relex.intertrust.set.shared;

import java.io.Serializable;
import java.util.List;

import static ru.relex.intertrust.set.server.GameStateConstants.ALL_CARDS_COUNT;
import static ru.relex.intertrust.set.server.GameStateConstants.SET_SIZE;

public class GameInfo implements Serializable {
    private Deck deck;              // колода
    private List<Card> cardsOnDesk; // карты на столе
    private Players players;
    private String currentUserName;
    // отрицательное значение - время до игры со знаком минус, положительное и 0 - время в игре
    private long time;
    private boolean started;
    private boolean prepared;
    private FinishCause finishCause;
    private boolean isObserveMode;

    public GameInfo() {
    }

    public boolean isCurrentUserInGame() {
        return currentUserName != null && !currentUserName.isEmpty();
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public int getDeckSize() {
        return deck.size();
    }

    public int getSetsCount() {
        return (ALL_CARDS_COUNT - getDeckSize() - cardsOnDesk.size()) / SET_SIZE;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public List<Card> getCardsOnDesk() {
        return cardsOnDesk;
    }

    public void setCardsOnDesk(List<Card> cardsOnDesk) {
        this.cardsOnDesk = cardsOnDesk;
    }

    public Players getPlayers() {
        return players;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public FinishCause getFinishCause() {
        return finishCause;
    }

    public void setFinishCause(FinishCause finishCause) {
        this.finishCause = finishCause;
    }

    public boolean isObserveMode() {
        return isObserveMode;
    }

    public void setObserveMode(boolean observeMode) {
        isObserveMode = observeMode;
    }
}
