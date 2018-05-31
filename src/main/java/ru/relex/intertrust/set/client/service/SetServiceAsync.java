package ru.relex.intertrust.set.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameInfo;

public interface SetServiceAsync
{

    /**
     * Регистрация игрока в игре.
     * Проверяется имя игрока на уникальность,
     * проверяется уникальность сессии и состояние игры.
     * Регистрация игрока происходит, если все введенные данные корректны и игра еще не идет.
     *
     * @param name имя игрока
     */
    void login(String name, String gameRoom, AsyncCallback<Boolean> async);

    /**
     * Метод, реализующий ПАС.
     * Добавление игрока в список спасовавших игроков и получение количества карт в колоде
     * клиента для проверки состояния икрока.
     *
     * @param cardsInDeck кол-во карт в колоде
     */
    void pass(int cardsInDeck, String gameRoom, AsyncCallback<Void> async);

    /**
     * Метод проверяет полученные карты на наличие в них сета.
     *
     * @param set полученные на проверку карты
     * @return true, если это сет
     */
    void checkSet(Card[] set, String gameRoom, AsyncCallback<Boolean> async);


    /**
     * @return возвращает описание состояния игры
     */
    void getGameInfo(String gameRoom, AsyncCallback<GameInfo> async);

    /**
     * Выход из игры.
     * Удаление игрока из списка игроков и проверяет, остались ли ещё в активных игроках люди.
     * Если нет, то создается новая игра.
     */
    void exit(String gameRoom, AsyncCallback<Void> async);

    void observe(String gameRoom, AsyncCallback<Void> async);
}
