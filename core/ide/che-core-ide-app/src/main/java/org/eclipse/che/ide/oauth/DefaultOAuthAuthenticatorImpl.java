/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.oauth;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import org.eclipse.che.ide.CoreLocalizationConstant;
import org.eclipse.che.ide.api.oauth.OAuth2Authenticator;
import org.eclipse.che.ide.ui.dialogs.CancelCallback;
import org.eclipse.che.ide.ui.dialogs.ConfirmCallback;
import org.eclipse.che.ide.ui.dialogs.DialogFactory;
import org.eclipse.che.security.oauth.JsOAuthWindow;
import org.eclipse.che.security.oauth.OAuthCallback;
import org.eclipse.che.security.oauth.OAuthStatus;

import javax.validation.constraints.NotNull;

/**
 * Default implementation of authenticator, used when no provider-specific one is present.
 *
 * @author Max Shaposhnik
 */
public class DefaultOAuthAuthenticatorImpl implements OAuth2Authenticator, OAuthCallback {
    AsyncCallback<OAuthStatus> callback;

    private final DialogFactory            dialogFactory;
    private final CoreLocalizationConstant localizationConstant;
    private       String                   authenticationUrl;

    @Inject
    public DefaultOAuthAuthenticatorImpl(DialogFactory dialogFactory,
                                         CoreLocalizationConstant localizationConstant) {
        this.dialogFactory = dialogFactory;
        this.localizationConstant = localizationConstant;
    }

    @Override
    public void authorize(String authenticationUrl, @NotNull final AsyncCallback<OAuthStatus> callback) {
        this.authenticationUrl = authenticationUrl;
        this.callback = callback;
        showDialog();
    }

    private void showDialog() {
        dialogFactory.createConfirmDialog(localizationConstant.authorizationDialogTitle(), localizationConstant.authorizationDialogText(), new ConfirmCallback() {
            @Override
            public void accepted() {
                showAuthWindow();
            }
        }, new CancelCallback() {
            @Override
            public void cancelled() {
                callback.onSuccess(OAuthStatus.NOT_PERFORMED);
            }
        }).show();
    }

    @Override
    public String getProviderName() {
        return "default";
    }


    @Override
    public void onAuthenticated(OAuthStatus authStatus) {
        callback.onSuccess(authStatus);
    }

    private void showAuthWindow() {
        JsOAuthWindow authWindow;
        authWindow = new JsOAuthWindow(authenticationUrl, "error.url", 500, 980, this);
        authWindow.loginWithOAuth();
    }
}
