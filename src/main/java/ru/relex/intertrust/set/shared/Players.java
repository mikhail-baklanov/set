package ru.relex.intertrust.set.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Players implements Serializable {
    private List<Player> players = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Players))
            return false;
        Players other = (Players) obj;
        if (players.size() != other.players.size())
            return false;
        for (int i = 0; i < players.size(); i++)
            if (!players.get(i).equals(other.players.get(i)))
                return false;
        return true;
    }

    public void removePlayer(String name) {
        players.remove(getPlayerByName(name));
    }

    public void setInactive(String name) {
        players.forEach(p -> {
            if (name.equals(p.getName())) p.setActive(false);
        });
        rearrange();
    }

    public void resetPassed() {
        players.forEach(p -> p.setPassed(false));
    }

    public void addPlayer(String name) {
        players.add(new Player(name));
        rearrange();
    }

    public boolean hasActivePlayer() {
        return players.stream().anyMatch(Player::isActive);
    }

    public boolean hasPlayer(String name) {
        return players.stream().anyMatch(p -> name.equals(p.getName()));
    }

    public Player getPlayerByName(String name) {
        return players.stream().filter(p -> name.equals(p.getName())).findFirst().orElse(null);
    }

    public int getActivePlayersCount() {
        return (int) players.stream().filter(Player::isActive).count();
    }

    private void rearrange() {
        players.sort((p1, p2) -> {
            if (p1.isActive() != p2.isActive())
                return p1.isActive() ? 1 : -1;
            if (p1.getScore() != p2.getScore())
                return p1.getScore() - p2.getScore();
            else
                return p1.getName().compareTo(p2.getName());
        });
    }

    public void reset() {
        players.clear();
    }

    public List<Player> getPlayerList() {
        return players;
    }

    public int getPassedPlayersCount() {
        return (int) players.stream().filter(Player::isPassed).count();
    }
}
