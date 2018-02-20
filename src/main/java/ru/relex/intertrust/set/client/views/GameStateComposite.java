package ru.relex.intertrust.set.client.views;

import com.google.gwt.user.client.ui.Composite;
import ru.relex.intertrust.set.shared.GameState;

public abstract class GameStateComposite extends Composite {
    public abstract void setGameState(GameState gameState);
}
