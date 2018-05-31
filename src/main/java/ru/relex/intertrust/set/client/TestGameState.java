package ru.relex.intertrust.set.client;

import ru.relex.intertrust.set.server.GameState;

import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

class TestGameState {

    /**
     * Имя текущего игрока
     */
    private static final String CURRENT_PLAYER_NAME = "Ivan";

    /**
     * Имя другого игрока
     */
    private static final String SAMPLE_PLAYER_NAME = "Someone";

    /**
     * Количество добавляемых карт для новой игры
     */
    private static final int INITIAL_COUNT_OF_CARDS = 12;

    /**
     * Количество добавляемых карт при пасе
     */
    private static final int PASS_COUNT_OF_CARDS = 3;

    //region Game states

    /**
     * Начальное состояние игры, когда в ней еще нет игроков
     */
    static GameState getInitialGameState() {
        return new GameState();
    }

//    /**
//     * Начальное состояние игры, когда текущий игрок еще не зарегистрирован,
//     * но другой игрок зарегистрирован, и на экране появляется таймер до начала игры
//     */
//    static GameState getInitialGameStateWithTimer() {
//        return getGameStateWithPlayer(SAMPLE_PLAYER_NAME);
//    }
//    /**
//     * Состояние игры, когда текущий игрок не зарегистрирован, но игра уже идет
//     */
//    static GameState getAnotherGameState() {
//        GameState gameState = getGameStateWithPlayer(SAMPLE_PLAYER_NAME);
//        return initRunningGameState(gameState);
//    }
//
//    /**
//     * Состояние игры, когда текущий игрок зарегистрирован,
//     * но идет ожидание других игроков
//     */
//    static GameState getWaitingGameState() {
//        return getGameStateWithPlayer(CURRENT_PLAYER_NAME);
//    }
//
//    /**
//     * Состояние игры, когда текущий игрок зарегистрирован, и игра уже идет
//     */
//    static GameState getRunningGameState() {
//        GameState gameState = initRunningGameState(getWaitingGameState());
//        return addCards(gameState, INITIAL_COUNT_OF_CARDS);
//    }
//
//    /**
//     * Состояние запущенной игры, когда игрок сделал пас
//     * @param gameState Состояние игры, при котором игрок делает пас
//     */
//    static GameState getPassGameState(GameState gameState) {
//        return addCards(gameState, PASS_COUNT_OF_CARDS);
//    }
//    //endregion
//
//    /**
//     * Функция, возвращающая имя текущего игрока
//     * @return Имя текущего игрока
//     */
//    static String getCurrentPlayerName() {
//        return CURRENT_PLAYER_NAME;
//    }
//
////    /**
////     * Вспомогательная функция, возвращающая новое состояние игры
////     * с добавленным игроком
////     * @param playerName Имя игрока
////     * @return Состояние игры с одним добавленным игроком
////     */
////    private static GameState getGameStateWithPlayer(String playerName) {
////        GameState gameState = new GameState();
////        gameState.loginPlayer(playerName);
////        gameState.setActivePlayers(gameState.getActivePlayersCount() + 1);
////        return gameState;
////    }
//
//    /**
//     * Вспомогательная функция инициализации запущенной игры
//     * @param gameState Состояние запущенной игры
//     * @return Инициализированное состояние игры
//     */
//    private static GameState initRunningGameState(GameState gameState) {
//        gameState.setStart(true);
//        gameState.setTime(1);
//        return gameState;
//    }
//
//    /**
//     * Вспомогательная функция добавления карт на игровой стол
//     * @param gameState Состояние запущенной игры
//     * @param countOfCards Количество добавляемых карт
//     * @return Новое состояние игры
//     */
//    private static GameState addCards(GameState gameState, int countOfCards) {
//        Deck deck = gameState.getDeck();
//        for (int i = 0; i < countOfCards; i++) {
//            Card cardInDeck = deck.pop();
//            gameState.getCardsOnDesk().add(cardInDeck);
//        }
//        return gameState;
//    }
//
//    private NextState nextState = new NextState();
//    /**
//     * Экспериментальный класс для работы с состояниями игры
//     */
//    static class NextState{
//        int counter = 0;
//        int ticValue = 5;
//        GameState runningGameState = TestGameState.getRunningGameState();
//        GameState states[] = {
//                TestGameState.getInitialGameState(),
//                TestGameState.getInitialGameStateWithTimer(),
//                TestGameState.getAnotherGameState(),
//                TestGameState.getWaitingGameState(),
//                runningGameState
//        };
//        boolean isCurrentPlayerRegistered;
//        int index = 0;
//
//        /**
//         * Функция получения состояния игры
//         * @return текущее состояние игры
//         */
//        GameState get() {
//            GameState gameState;
//            isCurrentPlayerRegistered = index > 1;
//            if (index >= states.length) {
//                gameState = TestGameState.getPassGameState(runningGameState);
//                //gameState = states[states.length - 1];
//            } else {
//                gameState = states[index];
//                if (counter == 0){
//                    index++;
//                    if (index < states.length){
//                        counter = ticValue;
//                    }
//                } else {
//                    counter--;
//                }
//            }
//            consoleLog("Фейковое состояние №"+index);
//            return gameState;
//        }
//    }
//
//    private String playerName;
//    /**
//     * Экспериментальная функция по получению состояния игры
//     * @return текущее состояние игры
//     */
//    private GameState getTestGameState() {
//        GameState gameState = nextState.get();
//        playerName = nextState.isCurrentPlayerRegistered
//                ? TestGameState.getCurrentPlayerName()
//                : null;
//        return gameState;
//    }
}
