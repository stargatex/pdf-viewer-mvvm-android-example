package com.stargatex.lahiru.basicpdfviwer.di.module.network;


import static com.stargatex.lahiru.basicpdfviwer.utils.constant.SecConstants.IDS_OAUTH2_CLIENT_ID;
import static com.stargatex.lahiru.basicpdfviwer.utils.constant.SecConstants.IDS_OAUTH2_CLIENT_SECRET;

import android.util.Base64;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
@Singleton
public class IdsHttpsInterceptor implements Interceptor {
    private final String basicAuthString = IDS_OAUTH2_CLIENT_ID + ":" + IDS_OAUTH2_CLIENT_SECRET;
    private final byte[] basicAuthBytesEncy = Base64.encode(basicAuthString.getBytes(), Base64.NO_WRAP);
    private final String basicAuthStringEncy = new String(basicAuthBytesEncy);


    @Inject
    public IdsHttpsInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .addHeader("Authorization", "Basic " + basicAuthStringEncy)
                .build();

        Response response = chain.proceed(request);
        String bodyString;
        bodyString = response.body().string();
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
    }
}
