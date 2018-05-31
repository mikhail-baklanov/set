package ru.relex.intertrust.set.server;

public interface GameStateConstants {

    // миллисекунд в одной секунде
    long SEC_MS = 1000;

    // условное значение момента времени, когда игра неактивна
    long INACTIVE_GAME_TIME = -1;

    // период неактивности игроков, после которого игра автоматически завершается
    long INACTIVITY_PERIOD_MS = 600 * SEC_MS;

    // время до начала игры после подключения первого игрока
    long TIME_TO_GAME_MS = 10 * SEC_MS;

    // частота обновления синтетического времени в миллисекундах
    int TICK_MS = 1000;

    // размер сета
    int SET_SIZE = 3;

    // общее число карт
    int ALL_CARDS_COUNT = 81;

    // начальное количество карт на игровом столе
    int INITIAL_NUMBER_OF_CARDS = 12;

    // максимальное число карт на столе. Анализ карт утверждает, что в данном количестве карт всегда есть сет
    int MAX_NUMBER_OF_CARDS = 21;

    // штраф за неверно названный сет
    int PENALTY = -5;

    // награда за верно названный сет
    int REWARD = 3;
}
