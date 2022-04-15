package com.stargatex.lahiru.basicpdfviwer.di.module.network;


import com.stargatex.lahiru.basicpdfviwer.di.manager.auth.UserManager;

import java.io.IOException;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public class ResourceHttpsInterceptor implements Interceptor {
    private final UserManager userManager;


    @Inject
    public ResourceHttpsInterceptor(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder builder = request.newBuilder();
        setAuthHeaders(builder, userManager.getToken());

        request = builder.cacheControl(CacheControl.FORCE_NETWORK).build();
        Response proceed = chain.proceed(request);

        if (proceed.code() == HttpsURLConnection.HTTP_OK) {
            ResponseBody body = proceed.body();

            return proceed.newBuilder()
                    .body(body)
                    .build();

        } else if (proceed.code() == HttpsURLConnection.HTTP_NOT_FOUND) {
            ResponseBody body = proceed.body();

            return proceed.newBuilder()
                    .body(ResponseBody.create(proceed.body().contentType(), body.toString()))
                    .build();
        } else {
            return proceed;
        }

    }

    private void setAuthHeaders(Request.Builder builder, String token) {
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        builder.header("Connection", "close");
    }


}
