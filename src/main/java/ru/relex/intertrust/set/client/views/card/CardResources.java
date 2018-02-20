package ru.relex.intertrust.set.client.views.card;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface CardResources extends ClientBundle {
    CardResources INSTANCE = GWT.create(CardResources.class);

    interface CardStyles extends CssResource {
        String visible();

        String shape();

        String active();

        @ClassName("not-active")
        String notActive();

        String card();
    }

        @Source("Card.gss")
        CardStyles style();
}
