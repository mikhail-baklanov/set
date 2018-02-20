package ru.relex.intertrust.set.client.views.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface LoginResources extends ClientBundle {
    LoginResources INSTANCE = GWT.create(LoginResources.class);

    interface LoginStyles extends CssResource {
        @ClassName("nickname-wrapper")
        String nicknameWrapper();

        @ClassName("login-block_new-player")
        String loginBlock_newPlayer();

        String bar();

        @ClassName("login-block_nickname")
        String loginBlock_nickname();

        @ClassName("login-block_hello")
        String loginBlock_hello();

        @ClassName("login-block_game-start")
        String loginBlock_gameStart();

        String active();

        @ClassName("nickname-wrapper_error")
        String nicknameWrapper_error();

        @ClassName("login-block_continue")
        String loginBlock_continue();
    }

        @Source("Login.gss")
        LoginStyles style();
}
