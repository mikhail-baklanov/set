package ru.relex.intertrust.set.client.views.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.shared.Card;

/**
 *  View карты.
 */
public class CardView extends Composite {

    interface CardViewUiBinder extends UiBinder<Widget, CardView> {
    }

    private static CardView.CardViewUiBinder uiBinder = GWT.create(CardView.CardViewUiBinder.class);

    /**
     *  Контейнер для фигур.
     */
    @UiField
    HTMLPanel cardContainer;

    /**
     *  Константы для создания пути к картинке.
     */
    private static String FILL[] = {"H", "O", "S"};
    private static String COLOR[] = {"g", "p", "r"};
    private static String SHAPE[] = {"D", "P", "S"};

    /**
     *  Карта, которую нужно отобразить.
     */
    private Card card;

    public CardView(Card card) {
        this.card = card;
        CardResources.INSTANCE.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
        for (int i=1;i<=card.getShapeCount();i++) {
            HTML shape = new HTML("");
            shape.getElement().setClassName("shape");
            shape.getElement().getStyle().setBackgroundImage("url('images/icons/1"+
                    FILL[card.getFill()-1]+
                    COLOR[card.getColor()-1]+
                    SHAPE[card.getShape()-1]+".svg')");
            cardContainer.add(shape);
        }
    }

    /**
     *  Получение карты.
     *
     *  @return карту
     */
    public Card getCard () {
        return card;
    }
}

