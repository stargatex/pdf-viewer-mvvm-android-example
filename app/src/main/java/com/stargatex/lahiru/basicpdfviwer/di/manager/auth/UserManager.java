package com.stargatex.lahiru.basicpdfviwer.di.manager.auth;



import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.app.AccManagerDef.*;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityCompat;


import com.stargatex.lahiru.basicpdfviwer.data.model.LoginResponse;
import com.stargatex.lahiru.basicpdfviwer.ui.login.LoginActivity;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;


/**
 * @author Lahiru Jayawickrama (LahiruJaya)
 * @version 1.0
 */
@Singleton
public class UserManager {
    private Context context;

    private final AccountManager accountManager;

    @Inject
    public UserManager(AccountManager accountManager, Context context) {
        this.context = context;
        this.accountManager = accountManager;
    }


    public Account getAccount() {
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        Timber.d("getAccount accounts.length %s", accounts.length);
        if (accounts.length == 0) {
            return null;
        }
        return accounts[0];
    }

    public void connectAccount(String username, String password, LoginResponse login) {
        Account account = getAccount();
        if (account == null)
            createAccount(username.toLowerCase().trim(), password, null);

        account = getAccount();
        long mSec = System.currentTimeMillis() + (login.getExpiresIn() * 1000 * 20);
        accountManager.setPassword(account, password);
        accountManager.setUserData(account, KEY_EXPIRE_TIME, String.valueOf(mSec));
        accountManager.setAuthToken(account, DEFAULT_AUTH_TOKEN_TYPE, login.getAccessToken());
        accountManager.setUserData(account, KEY_REFRESH_TOKEN, login.getRefreshToken());
        accountManager.setUserData(account, KEY_USERNAME, username.toUpperCase().trim());

    }

    private void createAccount(String username, String password, LoginResponse login) {
        if (login != null && login.getAccessToken() != null) {
            connectAccount(username, password, login);
        } else if (accountManager.addAccountExplicitly(new Account(ACCOUNT, ACCOUNT_TYPE), null, null)) {
            Timber.d("Add Account Explicitly: Success!");
        } else {
            throw new IllegalStateException("Add Account Failed!");
        }

    }

    public void removeAccount(Activity context) {
        Account account = getAccount();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            accountManager.removeAccountExplicitly(account);
        }*/
        accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
            @Override
            public void run(AccountManagerFuture<Boolean> accountManagerFuture) {
                try {
                    Timber.d("accountManagerFuture " + accountManagerFuture.getResult());
                    ActivityCompat.finishAffinity(context);
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                }
            }
        }, null);

    }

    public void removeAccountOnly(Activity context) {
        Account account = getAccount();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            accountManager.removeAccountExplicitly(account);
        }*/
        accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
            @Override
            public void run(AccountManagerFuture<Boolean> accountManagerFuture) {
                try {
                    Timber.d("accountManagerFuture %s", accountManagerFuture.getResult());
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                }
            }
        }, null);

    }

    public void removeAccount() {
        Account account = getAccount();
        if (account != null) {
            accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                @Override
                public void run(AccountManagerFuture<Boolean> accountManagerFuture) {
                    try {
                        Timber.d("accountManagerFuture %s", accountManagerFuture.getResult());
                        if (accountManagerFuture.getResult()) {

                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    } catch (AuthenticatorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (OperationCanceledException e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        }

    }

    public String getToken() {
        Account account = getAccount();
        return (account != null) ? accountManager.peekAuthToken(account, DEFAULT_AUTH_TOKEN_TYPE) : null;
    }

    public String getRefreshToken() {
        Account account = getAccount();
        return accountManager.getUserData(account, KEY_REFRESH_TOKEN);
    }

    public String getUsername() {
        Account account = getAccount();
        return (account != null) ? accountManager.getUserData(account, KEY_USERNAME) : null;
    }

    public String getTokenExpireIn() {
        Account account = getAccount();
        return accountManager.getUserData(account, KEY_EXPIRE_TIME);
    }

    public void setRefreshTokenData(LoginResponse login) {
        Account account = getAccount();
        long mSec = System.currentTimeMillis() + (login.getExpiresIn() * 1000 * 20);
        accountManager.setUserData(account, KEY_EXPIRE_TIME, String.valueOf(mSec));
        accountManager.setAuthToken(account, DEFAULT_AUTH_TOKEN_TYPE, login.getAccessToken());
        accountManager.setUserData(account, KEY_REFRESH_TOKEN, login.getRefreshToken());
    }


}
