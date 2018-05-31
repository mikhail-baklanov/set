package ru.relex.intertrust.set.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.relex.intertrust.set.server.GameStateConstants.ALL_CARDS_COUNT;

/**
 * Колода карт.
 */
public class Deck implements Serializable {

    private List<Card> cards = new ArrayList<>();

    /**
     * Конструктор собирает колоду из 81 неповторяющихся карт и
     * перемешивает ее.
     */
    public Deck() {
        reset();
    }

    public void reset() {
        cards.clear();
        for (int cardNumber = 0; cardNumber < ALL_CARDS_COUNT; cardNumber++) {
            Card card = new Card(
                    cardNumber / 27 + 1,
                    (cardNumber % 9) / 3 + 1,
                    (cardNumber%27) / 9+1,
                    cardNumber % 3+1);
            cards.add(card);
        }
        Collections.shuffle(cards);
    }

    public Card pop() {
        Card c = cards.get(cards.size()-1);
        cards.remove(c);
        return c;
    }

    public boolean empty() {
        return cards.size()==0;
    }

    public int size() {
        return cards.size();
    }
}
