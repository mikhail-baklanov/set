package ru.relex.intertrust.set.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

public interface SetServiceAsync
{

    /**
     * Выход из игры.
     * Удаление игрока из списка игроков и проверяет, остались ли ещё в активных игроках люди.
     * Если нет, то создается новая игра.
     */
    void exit(String id, AsyncCallback<Void> async);

    /**
     * Метод, реализующий ПАС.
     * Добавление игрока в список спасовавших игроков и получение количества карт в колоде
     * клиента для проверки состояния икрока.
     *
     * @param cardsInDeck кол-во карт в колоде
     */
    void pass(int cardsInDeck, String id, AsyncCallback<Void> async);

    /**
     * Метод проверяет полученные карты на наличие в них сета.
     *
     * @param set полученные на проверку карты
     * @return true, если это сет
     */
    void checkSet(Card[] set, String id, AsyncCallback<Boolean> async);


    /**
     * Регистрация игрока в игре.
     * Проверяется имя игрока на уникальность,
     * проверяется уникальность сессии и состояние игры.
     * Регистрация игрока происходит, если все введенные данные корректны и игра еще не идет.
     *
     * @param name имя игрока
     * @return true, если регистрация прошла успешно
     */
    void login(String name, String id, AsyncCallback<String> async);

    /**
     * @return возвращает описание состояния игры
     */
    void getGameState(String gameRoom, AsyncCallback<GameState> async);
}
