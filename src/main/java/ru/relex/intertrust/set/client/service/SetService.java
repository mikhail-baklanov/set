package ru.relex.intertrust.set.client.service;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameInfo;

@RemoteServiceRelativePath("service")
public interface SetService extends RemoteService
{
    /**
     * Регистрация игрока в игре.
     * Проверяется имя игрока на уникальность,
     * проверяется уникальность сессии и состояние игры.
     * Регистрация игрока происходит, если все введенные данные корректны и игра еще не идет.
     *
     * @param name имя игрока
     * @return true, если регистрация прошла успешно
     */
    boolean login(String name, String gameRoom);

    /**
     * @return возвращает описание состояния игры
     */
    GameInfo getGameInfo(String gameRoom);

    /**
     * Метод проверяет полученные карты на наличие в них сета.
     *
     * @param set полученные на проверку карты
     * @return true, если это сет
     */
    boolean checkSet(Card[] set, String gameRoom);

    /**
     * Метод, реализующий ПАС.
     * Добавление игрока в список спасовавших игроков и получение количества карт в колоде
     * клиента для проверки состояния икрока.
     *
     * @param cardsInDeck кол-во карт в колоде
     */
    void pass(int cardsInDeck, String gameRoom);

    /**
     * Выход из игры.
     * Удаление игрока из списка игроков и проверяет, остались ли ещё в активных игроках люди.
     * Если нет, то создается новая игра.
     */
    void exit(String gameRoom);

    void observe(String gameRoom);
}
