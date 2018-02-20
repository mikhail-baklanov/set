package ru.relex.intertrust.set.client.views.gamefield;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.uiHandlers.GameFieldViewUIHandler;
import ru.relex.intertrust.set.client.constants.GameLocale;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.client.views.GameStateComposite;
import ru.relex.intertrust.set.client.views.card.CardView;
import ru.relex.intertrust.set.shared.Card;
import ru.relex.intertrust.set.shared.GameState;

import java.util.ArrayList;
import java.util.List;

/**
 *  View для игрового поля.
 */
public class GameFieldView extends GameStateComposite{

    interface StartViewUiBinder extends UiBinder<Widget, GameFieldView>{ }

    private GameLocale                 gameLocale   =   GWT.create(GameLocale.class);
    private GameFieldResources         gfr          =   GWT.create(GameFieldResources.class);
    private static StartViewUiBinder   uiBinder     =   GWT.create(StartViewUiBinder.class);

    /**
     *  Контейнер для виджетов карт.
     */
    @UiField
    HTMLPanel cardContainer;

    /**
     *  Контейнер для отображения собранных сетов.
     */
    @UiField
    SpanElement countOfSets;

    /**
     *  Контейнер для статистики игроков.
     */
    @UiField
    FlowPanel statisticContainer;

    /**
     *  Прошедшее время с начала игры.
     */
    @UiField
    SpanElement time;

    /**
     *  Левый блок игрового поля.
     */
    @UiField
    HTMLPanel leftBar;

    /**
     *  Правый блок игрового поля.
     */
    @UiField
    HTMLPanel rightBar;

    /**
     *  Оставшиеся карты.
     */
    @UiField
    SpanElement cardLeft;

    /**
     *  Кнопка для скрытия правого бара.
     */
    @UiField
    SimplePanel slideButton;

    /**
     *  Кнопка для пасса.
     */
    @UiField
    Button passButton;

    /**
     *  Кнопка для выхода из игры.
     */
    @UiField
    Button exitGame;

    /**
     * Кнопка для смены режима отображения.
     */
    @UiField
    Button changeMode;

    /**
     *  Контейнеры для статичного текста.
     */
    @UiField
    DivElement statistic;

    @UiField
    SpanElement players;

    @UiField
    SpanElement gamePoints;

    @UiField
    SpanElement cardLeftSpan;

    @UiField
    SpanElement countOfSetsLabel;

    @UiField
    SpanElement timeLabel;

    /**
     *  Необходимые для использования стили.
     */
    private static GameFieldResources.GameFieldStyles style = GameFieldResources.INSTANCE.style();

    /**
     *  Текущее состояние игры.
     */
    private GameState currentGameState = new GameState();

    /**
     *  Массив выбранных игроком карт.
     */
    private List<CardView> choosedCards = new ArrayList<>();

    /**
     *  Обработчик GameFieldView.
     */
    private GameFieldViewUIHandler uiHandler;

    public GameFieldView(GameFieldViewUIHandler uiHandler) {
        this.uiHandler = uiHandler;
        GameFieldResources.INSTANCE.style().ensureInjected();

        initWidget(uiBinder.createAndBindUi(this));
        initViewWidgets();

        slideButton.sinkEvents(Event.ONCLICK);
        slideButton.addHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!leftBar.getElement().hasClassName("active")) {
                    leftBar.getElement().addClassName("active");
                    rightBar.getElement().addClassName("active");
                } else {
                    leftBar.getElement().removeClassName("active");
                    rightBar.getElement().removeClassName("active");
                }
            }
        }, ClickEvent.getType());
    }

    @UiHandler("exitGame")
    public void onClickExit(ClickEvent e) {
        if (uiHandler.canChange(currentGameState)) {   //Одна кнопка используется и для переключения режимов
            uiHandler.changeMode();                    //Необходимо сделать отдельную кнопку под этот блок кода
            return;
        }
        uiHandler.exit();
    }

    @UiHandler("changeMode")
    public void onClickChangeMode(ClickEvent e) {
        if (uiHandler.canChange(currentGameState))
            uiHandler.changeMode();
    }

    @UiHandler("passButton")
    public void doClick(ClickEvent e) {
        if (uiHandler.canChange(currentGameState))
            return;
        uiHandler.pass(currentGameState.getDeck().size());
    }

    /**
     *  Метод, который заполняет View статичным текстом.
     */
    private void initViewWidgets() {
        this.statistic.setInnerHTML(gameLocale.statistic());
        this.exitGame.setHTML(gameLocale.exitGame());
        this.changeMode.setHTML(gameLocale.exitGame());//todo: Изменить константу
        this.players.setInnerHTML(gameLocale.players());
        this.gamePoints.setInnerHTML(gameLocale.gamePoints());
        this.passButton.setHTML(gameLocale.pass());
        this.cardLeftSpan.setInnerHTML(gameLocale.cardsInDeck() + ": ");
        this.countOfSetsLabel.setInnerHTML(gameLocale.setsCollected());
        this.timeLabel.setInnerHTML(gameLocale.timeLabel());
    }

    /**
     *  Актуализация прошедшего с начала игры времени.
     *
     *  @param time время
     */
    private void setTime(String time){
        this.time.setInnerHTML(time);
    }

    /**
     *  Актуализация количества оставшихся карт.
     *
     *  @param cardLeftCount количество оставшихся карт
     */
    private void setCardLeft(int cardLeftCount){
        if (cardLeft.getInnerHTML().equals("") || Integer.parseInt(cardLeft.getInnerHTML()) != cardLeftCount)
        this.cardLeft.setInnerHTML("" + cardLeftCount);
    }

    /**
     *  Актуализация статистики игроков.
     *
     *  @param nickNames имена игроков
     *  @param scores баллы игроков
     */
    private void setStatistics(List<String> nickNames, List<Integer> scores){
        if(statisticContainer.getWidgetCount() == 0) {
            for (int i = 0; i < nickNames.size(); i++) {
                HTML player = new HTML("<span>" + nickNames.get(i) + "</span><span>" + scores.get(i) + "</span>");
                player.setStyleName(style.statisticItem());
                this.statisticContainer.add(player);

                HTML separator = new HTML("");
                separator.setStyleName(style.separator());
                this.statisticContainer.add(separator);
            }
        } else {
            List<Integer> oldScores = currentGameState.getScore();
            List<HTML> players = new ArrayList<>();
            for (int i = 0; i < statisticContainer.getWidgetCount(); i += 2)
                players.add((HTML) statisticContainer.getWidget(i));

            for(int i = 0; i < players.size(); i++) {
                if (oldScores.get(i) != scores.get(i)) {
                    players.get(i).setHTML("<span>" + nickNames.get(i) + "</span><span>" + scores.get(i) + "</span>");
                }
                if (currentGameState.getNotAbleToPlay() != null && currentGameState.getNotAbleToPlay().contains(nickNames.get(i)))
                    players.get(i).addStyleName(style.passed());
                else
                    players.get(i).removeStyleName(style.passed());
                }
            }
        }

    /**
     *  Актуализация количества найденных сетов.
     *
     *  @param findSets количество найденных сетов
     */
    private void setHistory(int findSets){
            if (!countOfSets.getInnerHTML().equals("" + findSets))
                countOfSets.setInnerHTML("" + findSets);
    }

    /**
     *  Актуализация карт на столе.
     *
     *  @param newCardsOnDesk актуальные карты на столе
     */
    private void setCards(List<Card> newCardsOnDesk){
        boolean isActual;
        for (int i = 0; i < cardContainer.getWidgetCount(); i++) {
            isActual = false;
            CardView cardOnDesk = (CardView) cardContainer.getWidget(i);
            for (Card newCard: newCardsOnDesk) {
                if (cardOnDesk.getCard().equals(newCard)) {
                    isActual = true;
                    break;
                }
            }
            if (!isActual)
                removeFromDesk(cardOnDesk);
        }

        for (Card newCard: newCardsOnDesk) {
            isActual = false;
            for (int i = 0; i < cardContainer.getWidgetCount(); i++) {
                CardView cardOnDesk = (CardView) cardContainer.getWidget(i);
                if (newCard.equals(cardOnDesk.getCard())) {
                    isActual = true;
                    break;
                }
            }
            if (!isActual)
                addOnDesk(new CardView(newCard));
        }

    }

    /**
     *  Удаление карты со стола.
     *
     *  @param cardOnDesk карта
     */
    private void removeFromDesk(CardView cardOnDesk) {
        Timer timer = new Timer() {
            @Override
            public void run() {
                cardContainer.remove(cardOnDesk);
            }
        };
        cardOnDesk.getElement().addClassName("not-active");
        timer.schedule(300);
        choosedCards.remove(cardOnDesk);
    }

    /**
     *  Добавление карты на стол.
     *
     *  @param card карта
     */
    private void addOnDesk(CardView card) {
        ClickHandler click = new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                chooseCard(clickEvent.getSource());
            }
        };
        Timer timer = new Timer() {
            @Override
            public void run() {
                card.getElement().addClassName("visible");
            }
        };
        card.sinkEvents(Event.ONCLICK);
        card.addHandler(click, ClickEvent.getType());
        cardContainer.add(card);
        timer.schedule(300);
    }

    /**
     *  Актуализация всей информации.
     *
     *  @param gameState серверное состояние игры
     */
    @Override
    public void setGameState(GameState gameState) {
        if (uiHandler.canChange(gameState))
        {
            changeMode.removeStyleName(gfr.style().disable());
            changeMode.setStyleName(gfr.style().change_mode(), true);
            exitGame.setStyleName(gfr.style().disable(), true);
            passButton.setStyleName(gfr.style().disable(), true);
        } else {
            passButton.removeStyleName(gfr.style().disable());
            exitGame.removeStyleName(gfr.style().disable());
            exitGame.setStyleName(gfr.style().gameField_exit(), true);
            changeMode.setStyleName(gfr.style().disable(), true);
        }
        setCardLeft(gameState.getDeck().size());
        setCards(gameState.getCardsOnDesk());
        setHistory(gameState.getCountSets());
        setStatistics(gameState.getPlayers(), gameState.getScore());
        setTime(Utils.formatTime(gameState.getTime()));
        currentGameState = gameState;
    }

    /**
     *  Метод, который добавляет карту в массив выбранных карт.
     *  При выборе трёх карт вызывает обработчик для проверки сета и очищает массив.
     *
     *  @param widget карта
     */
    private void chooseCard (Object widget) {
        if (uiHandler.canChange(currentGameState))   //выделение карт не работает в режиме просмотра
            return;
        CardView card = (CardView) widget;
        if (!choosedCards.contains(card)) {
            card.getElement().addClassName("active");
            choosedCards.add(card);
            if (choosedCards.size() == 3) {
                Card[] cards = new Card[] {choosedCards.get(0).getCard(), choosedCards.get(1).getCard(), choosedCards.get(2).getCard()};
                uiHandler.checkSet(cards);
                for (CardView item: choosedCards)
                    item.getElement().removeClassName("active");
                choosedCards.clear();
            }
        } else {
            card.getElement().removeClassName("active");
            choosedCards.remove(card);
        }
    }

    /**
     *  Подсвечивание карт.
     *
     *  @param cards карты, которые необходимо подсветить
     */
    public void showNotCorrectCards(Card[] cards) {
        List<CardView> notCorrectCards = new ArrayList<>();
        for (int i = 0; i < cardContainer.getWidgetCount(); i++)
            for (Card card: cards) {
            CardView cardFromDesk = (CardView) cardContainer.getWidget(i);
                if (card.equals(cardFromDesk.getCard()))
                    notCorrectCards.add(cardFromDesk);
            }

        Timer timer = new Timer() {
            @Override
            public void run() {
                for (CardView card: notCorrectCards)
                    card.getElement().removeClassName("not-correct");
            }
        };

        for (CardView card: notCorrectCards)
            card.getElement().addClassName("not-correct");
        timer.schedule(500);
    }

    /**
     *  Метод, очищающий игровое поле.
     */
    public void clearGameField() {
        cardContainer.clear();
        statisticContainer.clear();
        currentGameState = new GameState();
}
}
