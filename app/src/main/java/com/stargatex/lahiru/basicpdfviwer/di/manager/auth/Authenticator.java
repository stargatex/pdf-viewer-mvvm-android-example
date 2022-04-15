package com.stargatex.lahiru.basicpdfviwer.di.manager.auth;



import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.app.AccManagerDef.*;


import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.stargatex.lahiru.basicpdfviwer.data.model.LoginResponse;
import com.stargatex.lahiru.basicpdfviwer.data.service.AuthApiService;
import com.stargatex.lahiru.basicpdfviwer.ui.login.LoginActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
@Singleton
public class Authenticator extends AbstractAccountAuthenticator {

    private static final String TAG = Authenticator.class.getSimpleName();
    private final Context context;
    private AccountManager accountManager;
    private AuthApiService authenticatorApiService;



    private Authenticator(Context context, AccountManager accountManager, AuthApiService authenticatorApiService) {
        super(context);
        this.context = context;
        this.accountManager = accountManager;
        this.authenticatorApiService = authenticatorApiService;
    }

    @Inject
    public Authenticator(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String accountType, String authTokenType, String[] requiredFeatures, Bundle bundle) throws NetworkErrorException {
        Bundle reply = new Bundle();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
        intent.putExtra(ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(ARG_AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(KEY_FEATURES, requiredFeatures);
        intent.putExtra(KEY_CREATE_ACCOUNT, true);

        reply.putParcelable(AccountManager.KEY_INTENT, intent);

        Timber.d("addAccount called");

        return reply;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String authTokenType, Bundle bundle) throws NetworkErrorException {
        String authToken = accountManager.peekAuthToken(account, authTokenType);
        if (authToken == null) {
            String password = accountManager.getPassword(account);
            try {
                authToken = ((LoginResponse)authenticatorApiService.login(GRANT_TYPE, account.name, password, SCOPE).blockingGet()).getAccessToken();
            } catch (Exception e) {
                e.printStackTrace();
               Timber.d( "getAuthToken %s", e.getMessage());

            }
        }

        if (authToken != null) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;

        }

        final Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
        intent.putExtra(ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(ARG_AUTH_TOKEN_TYPE, authTokenType);

        if (account != null) {
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account.name);
        }

        final Bundle res = new Bundle();
        res.putParcelable(AccountManager.KEY_INTENT, intent);

        return res;


    }

    @Override
    public String getAuthTokenLabel(String s) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        return null;
    }
}
