package com.stargatex.lahiru.basicpdfviwer.di.module.network;


import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.app.AccManagerDef.KEY_REFRESH_TOKEN;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stargatex.lahiru.basicpdfviwer.data.model.LoginResponse;
import com.stargatex.lahiru.basicpdfviwer.data.service.AuthApiService;
import com.stargatex.lahiru.basicpdfviwer.di.manager.auth.UserManager;

import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import timber.log.Timber;

/**
 * @author Lahiru Jayawickrama (LahiruJaya)
 * @version 1.0
 */
@Singleton
public class CustomAuthenticator implements Authenticator {
    private final UserManager userManager;
    private final AuthApiService authenticatorApiService;
    private final Context context;
    private int retries;

    @Inject
    public CustomAuthenticator(UserManager userManager, AuthApiService authenticatorApiService, Context context) {
        this.userManager = userManager;
        this.authenticatorApiService = authenticatorApiService;
        this.context = context;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {
        Objects.requireNonNull(response, "Response");
        int prvRetryCount = retryCount(response);
        return reAuthWithRefreshToken(response, prvRetryCount + 1);

    }

    private synchronized Request reAuthWithRefreshToken(Response response, int retry) {
        if (retry > 1) {
            Timber.d("authenticate: more than 1 tries giving up");
            return null;
        }
        String refreshToken = userManager.getRefreshToken();
        LoginResponse authResponse = authenticatorApiService
                .getTokenUsingRefreshToken(KEY_REFRESH_TOKEN, refreshToken)
                .blockingGet();

        if (authResponse != null && !authResponse.getAccessToken().isEmpty()) {
            userManager.setRefreshTokenData(authResponse);
            Request.Builder builder = response.request().newBuilder();
            setAuthHeaders(builder, userManager.getToken(), retry);
            return builder.build();
        }
        Timber.d("Failed to retrieve new token");
        return null;
    }

    private int retryCount(Response response) {
        return response != null && response.request() != null
                && response.header("appRetryCount") != null
                ? Integer.parseInt(response.header("appRetryCount")) : 0;
    }

    private void setAuthHeaders(Request.Builder builder, String token, int retry) {
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
            builder.header("appRetryCount", String.valueOf(retry));
        }
        builder.header("Connection", "close");
    }


}
