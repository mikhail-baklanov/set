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
import ru.relex.intertrust.set.client.l11n.GameStrings;
import ru.relex.intertrust.set.client.util.Utils;
import ru.relex.intertrust.set.client.views.ApplyGameInfoView;
import ru.relex.intertrust.set.client.views.card.CardView;
import ru.relex.intertrust.set.shared.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * View для игрового поля.
 */
public class GameFieldView extends ApplyGameInfoView {

    private static StartViewUiBinder uiBinder = GWT.create(StartViewUiBinder.class);
    /**
     * Необходимые для использования стили.
     */
    private static GameFieldResources.GameFieldStyles style = GameFieldResources.INSTANCE.style();
    /**
     * Контейнер для виджетов карт.
     */
    @UiField
    HTMLPanel cardContainer;
    /**
     * Контейнер для отображения собранных сетов.
     */
    @UiField
    SpanElement countOfSets;
    /**
     * Контейнер для статистики игроков.
     */
    @UiField
    FlowPanel statisticContainer;
    /**
     * Прошедшее время с начала игры.
     */
    @UiField
    SpanElement time;
    /**
     * Левый блок игрового поля.
     */
    @UiField
    HTMLPanel leftBar;
    /**
     * Правый блок игрового поля.
     */
    @UiField
    HTMLPanel rightBar;
    /**
     * Оставшиеся карты.
     */
    @UiField
    SpanElement cardLeft;
    /**
     * Кнопка для скрытия правого бара.
     */
    @UiField
    SimplePanel slideButton;
    /**
     * Кнопка для пасса.
     */
    @UiField
    Button passButton;
    /**
     * Кнопка для выхода из игры.
     */
    @UiField
    Button exitGame;
    /**
     * Контейнеры для статичного текста.
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
    private GameFieldResources gfr = GWT.create(GameFieldResources.class);
    /**
     * Текущее состояние игры.
     */
    private GameInfo gameInfo = new GameInfo();
    /**
     * Массив выбранных игроком карт.
     */
    private List<CardView> choosedCards = new ArrayList<>();
    /**
     * Обработчик GameFieldView.
     */
    private GameFieldViewUIHandler uiHandler;

    public GameFieldView(GameFieldViewUIHandler uiHandler) {
        this.uiHandler = uiHandler;
        GameFieldResources.INSTANCE.style().ensureInjected();

        initWidget(uiBinder.createAndBindUi(this));
        initViewWidgets();

        slideButton.sinkEvents(Event.ONCLICK);
        slideButton.addHandler(event -> {
            if (!leftBar.getElement().hasClassName("active")) {
                leftBar.getElement().addClassName("active");
                rightBar.getElement().addClassName("active");
            } else {
                leftBar.getElement().removeClassName("active");
                rightBar.getElement().removeClassName("active");
            }
        }, ClickEvent.getType());
    }

    @UiHandler("exitGame")
    public void onClickExit(ClickEvent e) {
        uiHandler.exit();
    }

    @UiHandler("passButton")
    public void doClick(ClickEvent e) {
        uiHandler.pass(gameInfo.getDeckSize());
    }

    /**
     * Метод, который заполняет View статичным текстом.
     */
    private void initViewWidgets() {
        GameStrings gameStrings = GWT.create(GameStrings.class);
        this.statistic.setInnerHTML(gameStrings.statistic());
        this.exitGame.setHTML(gameStrings.exitGame());
        this.players.setInnerHTML(gameStrings.players());
        this.gamePoints.setInnerHTML(gameStrings.gamePoints());
        this.passButton.setHTML(gameStrings.pass());
        this.cardLeftSpan.setInnerHTML(gameStrings.cardsInDeck());
        this.countOfSetsLabel.setInnerHTML(gameStrings.setsCollected());
        this.timeLabel.setInnerHTML(gameStrings.timeLabel());
    }

    /**
     * Актуализация прошедшего с начала игры времени.
     *
     * @param time время
     */
    private void setTime(String time) {
        this.time.setInnerHTML(time);
    }

    /**
     * Актуализация количества оставшихся карт.
     *
     * @param cardLeftCount количество оставшихся карт
     */
    private void setCardLeft(int cardLeftCount) {
        if (cardLeft.getInnerHTML().equals("") || Integer.parseInt(cardLeft.getInnerHTML()) != cardLeftCount)
            this.cardLeft.setInnerHTML("" + cardLeftCount);
    }

    /**
     * Актуализация статистики игроков.
     */
    private void setStatistics(Players players) {
        if (players.equals(gameInfo.getPlayers()))
            return;
        statisticContainer.clear();
        for (Player p : players.getPlayerList()) {
            HTML player = new HTML("<span>" + p.getName() + "</span><span>" + p.getScore() + "</span>");
            player.setStyleName(style.statisticItem());
            if (p.isPassed())
                player.addStyleName(style.passed());
            else
                player.removeStyleName(style.passed());
            statisticContainer.add(player);

            HTML separator = new HTML("");
            separator.setStyleName(style.separator());
            statisticContainer.add(separator);
        }
    }

    /**
     * Актуализация количества найденных сетов.
     *
     * @param findSets количество найденных сетов
     */
    private void setHistory(int findSets) {
        if (!countOfSets.getInnerHTML().equals("" + findSets))
            countOfSets.setInnerHTML("" + findSets);
    }

    /**
     * Актуализация карт на столе.
     *
     * @param newCardsOnDesk актуальные карты на столе
     */
    private void setCards(Collection<Card> newCardsOnDesk) {
        boolean isActual;
        for (int i = 0; i < cardContainer.getWidgetCount(); i++) {
            isActual = false;
            CardView cardOnDesk = (CardView) cardContainer.getWidget(i);
            for (Card newCard : newCardsOnDesk) {
                if (cardOnDesk.getCard().equals(newCard)) {
                    isActual = true;
                    break;
                }
            }
            if (!isActual)
                removeFromDesk(cardOnDesk);
        }

        for (Card newCard : newCardsOnDesk) {
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
     * Удаление карты со стола.
     *
     * @param cardOnDesk карта
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
     * Добавление карты на стол.
     *
     * @param card карта
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
     * Актуализация всей информации.
     *
     * @param gameInfo серверное состояние игры
     */
    @Override
    public void setGameInfo(GameInfo gameInfo) {
        if (gameInfo.isObserveMode()) {
            passButton.setStyleName(gfr.style().disable(), true);
        } else {
            passButton.removeStyleName(gfr.style().disable());
        }
        setCardLeft(gameInfo.getDeckSize());
        setCards(gameInfo.getCardsOnDesk());
        setHistory(gameInfo.getSetsCount());
        setStatistics(gameInfo.getPlayers());
        setTime(Utils.formatTime(gameInfo.getTime()));

        // обновляем поле после перерисовки, поскольку перерисовываются только значения,
        // отличающиеся от старых
        this.gameInfo = gameInfo;
    }

    /**
     * Метод, который добавляет карту в массив выбранных карт.
     * При выборе трёх карт вызывает обработчик для проверки сета и очищает массив.
     *
     * @param widget карта
     */
    private void chooseCard(Object widget) {
        if (gameInfo.isObserveMode())   //выделение карт не работает в режиме наблюдения
            return;
        CardView card = (CardView) widget;
        if (!choosedCards.contains(card)) {
            card.getElement().addClassName("active");
            choosedCards.add(card);
            if (choosedCards.size() == 3) {
                Card[] cards = new Card[]{choosedCards.get(0).getCard(), choosedCards.get(1).getCard(), choosedCards.get(2).getCard()};
                uiHandler.checkSet(cards);
                for (CardView item : choosedCards)
                    item.getElement().removeClassName("active");
                choosedCards.clear();
            }
        } else {
            card.getElement().removeClassName("active");
            choosedCards.remove(card);
        }
    }

    /**
     * Подсвечивание карт.
     *
     * @param cards карты, которые необходимо подсветить
     */
    public void showNotCorrectCards(Card[] cards) {
        List<CardView> notCorrectCards = new ArrayList<>();
        for (int i = 0; i < cardContainer.getWidgetCount(); i++)
            for (Card card : cards) {
                CardView cardFromDesk = (CardView) cardContainer.getWidget(i);
                if (card.equals(cardFromDesk.getCard()))
                    notCorrectCards.add(cardFromDesk);
            }

        Timer timer = new Timer() {
            @Override
            public void run() {
                for (CardView card : notCorrectCards)
                    card.getElement().removeClassName("not-correct");
            }
        };

        for (CardView card : notCorrectCards)
            card.getElement().addClassName("not-correct");
        timer.schedule(500);
    }

    /**
     * Метод, очищающий игровое поле.
     */
    public void clearGameField() {
        cardContainer.clear();
        statisticContainer.clear();
        gameInfo = new GameInfo();
    }

    interface StartViewUiBinder extends UiBinder<Widget, GameFieldView> {
    }
}
