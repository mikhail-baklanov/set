package ru.relex.intertrust.set.client.views.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import ru.relex.intertrust.set.client.uiHandlers.LoginViewUIHandler;
import ru.relex.intertrust.set.client.constants.GameLocale;
import ru.relex.intertrust.set.client.util.Utils;

import static ru.relex.intertrust.set.client.util.Utils.consoleLog;

public class LoginView extends Composite {

    interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {
    }

    private GameLocale gameLocale = GWT.create(GameLocale.class);

    private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);

    /**
     * Всплывающее окно с ошибкой.
     */
    @UiField
    SpanElement errorLogin;

    /**
     * Поле для ввода логина.
     */
    @UiField
    TextBox nicknameLogin;

    /**
     * Время до начала игры.
     */
    @UiField
    SpanElement loginTimer;

    /**
     * Окно с таймером до начала игры.
     */
    @UiField
    DivElement timeBlockLogin;

    /**
     * Форма для отправки никнейма.
     */
    @UiField
    FormPanel submitLoginForm;

    @UiField
    DivElement welcome;

    @UiField
    Button submitLogin;

    @UiField
    SpanElement timeToPlay;

    @UiField
    LabelElement name;

    /**
     * Обработчик события регистрации пользователя.
     */
    //try
    private LoginViewUIHandler loginListener;

    public LoginView(LoginViewUIHandler loginListener) {
        LoginResources.INSTANCE.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
        timeToPlay.setInnerHTML(gameLocale.timeBlock());
        errorLogin.setInnerHTML(gameLocale.errorLogin());
        welcome.setInnerHTML(gameLocale.welcome());
        submitLogin.setHTML(gameLocale.continueGame());
        name.setInnerHTML(gameLocale.nickname());
        nicknameLogin.getElement().setAttribute("required", "true");
        this.loginListener = loginListener;

        submitLoginForm.addSubmitHandler(new FormPanel.SubmitHandler() {
            /**
             * Метод, который при удачной регистрации пользователя вызывает uiHandlerInterfaces.
             * @param event событие
             */
            @Override
            public void onSubmit(FormPanel.SubmitEvent event) //??
            {
                String name = nicknameLogin.getValue();
                loginListener.login(name);
                    }
        });
    }

    public void showLoginError() {
                            errorLogin.addClassName("active");
                    }
    //try

    /**
     * Метод, который вызывает submit формы submitLoginForm.
     * @param e случайное событие click
     */
    @UiHandler("submitLogin")
    public void login(ClickEvent e) {
        submitLoginForm.submit();
    }

    /**
     * Метод, который удаляет CSS класс active у окна с ошибкой.
     * @param e случайное событие click
     */
    @UiHandler("nicknameLogin")
    public void removeError(ClickEvent e) {
        errorLogin.removeClassName("active");
    }

    /**
     * Метод, который добавляет CSS класс active окну с таймером до начала игры и задает ему время.
     * @param time время до начала игры
     */
    public void setLoginTimer(long time){
        timeBlockLogin.addClassName("active");
        loginTimer.setInnerHTML(Utils.formatTime(time));
    }

    /**
     * Метод, который удаляет CSS класс active у окна с таймером до начала игры.
     */
    public void removeLoginTimer(){
        timeBlockLogin.removeClassName("active");
    }
}
