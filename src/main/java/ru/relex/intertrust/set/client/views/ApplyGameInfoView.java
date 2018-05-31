package ru.relex.intertrust.set.client.views;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import ru.relex.intertrust.set.shared.GameInfo;

public abstract class ApplyGameInfoView extends Composite {
    public abstract void setGameInfo(GameInfo gameInfo);
}
