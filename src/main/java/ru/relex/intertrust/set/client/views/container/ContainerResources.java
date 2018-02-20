package ru.relex.intertrust.set.client.views.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ContainerResources extends ClientBundle {
    ContainerResources INSTANCE = GWT.create(ContainerResources.class);

    interface ContainerStyles extends CssResource {
        @ClassName("not-active")
        String notActive();

        @ClassName("container-block")
        String loginBlock();
    }

        @Source("Container.gss")
        ContainerStyles style();
}
