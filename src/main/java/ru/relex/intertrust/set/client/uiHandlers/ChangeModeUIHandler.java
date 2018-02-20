package ru.relex.intertrust.set.client.uiHandlers;

import ru.relex.intertrust.set.shared.GameState;

public interface ChangeModeUIHandler {
    /**
     * Сменить режим отображения информации
     */
    void changeMode();

    /**
     * Возможность изменить режим отображения в зависимости от текущего состояния игры
     * @param gameState состояние игры
     * @return true если можно изменить режим отображения, false если нет
     */
    boolean canChange(GameState gameState);
}
