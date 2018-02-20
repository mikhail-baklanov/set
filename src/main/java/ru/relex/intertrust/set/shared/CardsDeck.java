package ru.relex.intertrust.set.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CardsDeck - класс, содержащий информацию о колоде карт.
 */
public class CardsDeck {

    private static List<Card> cardsDeck=new ArrayList<>();

    /**
     * Метод startCardDeck() собирает колоду из 81 неповторяющейся карты из класса Card
     * Collections.shuffle(cardsDeck) - перемешивает собранную колоду
     */

    public List<Card> startCardsDeck() {
        if (cardsDeck.size()!=0) {
            cardsDeck=new ArrayList<>();
        }
        for (int cardNumber=0;cardNumber<=80;cardNumber++) {
            Card card=new Card(cardNumber / 27 + 1,
                    (cardNumber % 9) / 3 + 1,
                    (cardNumber%27) / 9+1,
                    cardNumber % 3+1);
            cardsDeck.add(card);
        }
        Collections.shuffle(cardsDeck);
        return cardsDeck;
    }
}
