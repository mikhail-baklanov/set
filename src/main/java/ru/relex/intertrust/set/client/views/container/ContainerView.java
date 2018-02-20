package ru.relex.intertrust.set.client.views.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.views.gamefield.GameFieldView;
import ru.relex.intertrust.set.client.views.result.ResultView;

/**
 *  Контейнер для всех View игры.
 */
public class ContainerView extends Composite {

    interface ContainerViewUiBinder extends UiBinder<Widget, ContainerView> {
    }

    private static ContainerView.ContainerViewUiBinder uiBinder = GWT.create(ContainerView.ContainerViewUiBinder.class);

    /**
     *  Контейнер для добавления виджетов.
     */
    @UiField
    HTMLPanel containerPanel;

    /**
     *  Главный блок со всем контентом.
     */
    @UiField
    HTMLPanel block;

    /**
     *  Необходимые для использования стили.
     */
    private ContainerResources.ContainerStyles style = ContainerResources.INSTANCE.style();

    public ContainerView () {
        ContainerResources.INSTANCE.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     *  Метод удаляет старый виджет из контейнера и добавляет новый.
     *  @param widget новый виджет
     */
    public void setView (Widget widget) {
        Timer timer = new Timer() {
            @Override
            public void run() {
                containerPanel.clear();
                containerPanel.add(widget);
                
                if(widget instanceof GameFieldView)
                    block.removeStyleName(style.loginBlock());
                else
                    block.setStyleName(style.loginBlock());

                containerPanel.getElement().removeClassName("not-active");
            }
        };
        if (containerPanel.getWidgetCount() != 0) {
            containerPanel.getElement().addClassName("not-active");
            timer.schedule(400);
        } else
            timer.run();
    }
}
