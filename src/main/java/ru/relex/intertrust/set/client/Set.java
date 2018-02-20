package ru.relex.intertrust.set.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.views.container.ContainerView;

import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

public class Set implements EntryPoint {

    private final ContainerView containerView = new ContainerView();

    /**
     *Точка входа проекта
     */
    public void onModuleLoad() {

        RootPanel.get("gwt-wrapper").add(containerView);
        SetPresenter setPresenter = new SetPresenter(containerView);
    }

}