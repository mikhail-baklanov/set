package ru.relex.intertrust.set.client.uiHandlers;

import ru.relex.intertrust.set.shared.Card;

public interface GameFieldViewUIHandler extends ExitGameUIHandler {
    /**
     * проверка массива аргументов, являются ли они сетом
     */
    void checkSet(Card[] cards);

    /**
     * Метод, реализующий ПАС.
     */
    void pass(int count);
}
