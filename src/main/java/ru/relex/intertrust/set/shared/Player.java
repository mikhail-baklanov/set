package ru.relex.intertrust.set.shared;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private boolean active;
    private int score;
    private boolean passed;

    public Player() {
    }

    public Player(String name) {
        if (name == null)
            throw new IllegalArgumentException("Player(name): name не может быть равно null");
        this.name = name;
        this.active = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Player))
            return false;
        Player player = (Player) obj;
        return name.equals(player.name) && active == player.active
                && score == player.score && passed == player.passed;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int delta) {
        this.score += delta;
    }
}
