package com.stargatex.lahiru.basicpdfviwer.di.module;


import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.network.NetConfigs.CONNECT_TIMEOUT;
import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.network.NetConfigs.HTTP_PROTOCOL;
import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.network.NetConfigs.KEEP_ALIVE_DURATION_MS;
import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.network.NetConfigs.MAX_IDLE_CONNECTIONS;
import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.network.NetConfigs.READ_TIMEOUT;
import static com.stargatex.lahiru.basicpdfviwer.utils.constant.SecConstants.SERVICES_BASE_URL;
import static com.stargatex.lahiru.basicpdfviwer.utils.constant.SecConstants.TRUST_KEY;

import android.content.Context;
import android.os.Build;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.security.ProviderInstaller;
import com.stargatex.lahiru.basicpdfviwer.BuildConfig;
import com.stargatex.lahiru.basicpdfviwer.R;
import com.stargatex.lahiru.basicpdfviwer.data.service.AuthApiService;
import com.stargatex.lahiru.basicpdfviwer.data.service.ResourceApiService;
import com.stargatex.lahiru.basicpdfviwer.di.module.network.CustomAuthenticator;
import com.stargatex.lahiru.basicpdfviwer.di.module.network.IdsHttpsInterceptor;
import com.stargatex.lahiru.basicpdfviwer.di.module.network.ResourceHttpsInterceptor;
import com.stargatex.lahiru.basicpdfviwer.di.module.network.RxErrorHandlingCallAdapterFactory;
import com.stargatex.lahiru.basicpdfviwer.di.qualifier.network.OkHttpClientAuth;
import com.stargatex.lahiru.basicpdfviwer.di.qualifier.network.OkHttpClientResources;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import timber.log.Timber;


/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
@Module(includes = {ViewModelModule.class})
public class NetworkModule {


    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder(ObjectMapper objectMapper) {
        //TODO: define base url <base_url>/<your-endpoint>
        return new Retrofit.Builder().baseUrl(SERVICES_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create());
    }

    @Singleton
    @Provides
    ObjectMapper provideObjectMapper() {
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Singleton
    @Provides
    @OkHttpClientAuth
    OkHttpClient provideOkHttpClient(IdsHttpsInterceptor idsHttpsInterceptor
            , HttpLoggingInterceptor httpLoggingInterceptor
            , HostnameVerifier hostnameVerifier
            , ConnectionPool connectionPool
            , SSLSocketFactory sslSocketFactory
            , X509TrustManager x509TrustManager) {

        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .hostnameVerifier(hostnameVerifier)
                .connectionPool(connectionPool)
                .addInterceptor(idsHttpsInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .build();

    }


    @Singleton
    @Provides
    @OkHttpClientResources
    OkHttpClient provideOkHttpResourceClient(ResourceHttpsInterceptor resourceHttpsInterceptor
            , HttpLoggingInterceptor httpLoggingInterceptor
            , CustomAuthenticator customAuthenticator
            , HostnameVerifier hostnameVerifier
            , ConnectionPool connectionPool
            , SSLSocketFactory sslSocketFactory
            , X509TrustManager x509TrustManager) {

        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .hostnameVerifier(hostnameVerifier)
                .connectionPool(connectionPool)
                .authenticator(customAuthenticator)
                .addInterceptor(resourceHttpsInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .sslSocketFactory(sslSocketFactory, x509TrustManager)
                .build();

    }

    @Singleton
    @Provides
    HostnameVerifier provideHostnameVerifier() {
        return (s, sslSession) -> (s.equals(sslSession.getPeerHost()));
    }

    @Singleton
    @Provides
    ConnectionPool provideConnectionPool() {
        return new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION_MS, TimeUnit.MILLISECONDS);
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor httpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel((BuildConfig.DEBUG)
                ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        return httpLoggingInterceptor;
    }

    @Singleton
    @Provides
    SSLSocketFactory provideSslSocketFactory(SSLContext sslContext) {
        return sslContext.getSocketFactory();
    }

    @Singleton
    @Provides
    SSLContext provideSslContext(Context context, X509TrustManager x509TrustManager) {
        try {
            ProviderInstaller.installIfNeeded(context);
            SSLContext sslContext = SSLContext.getInstance(HTTP_PROTOCOL);
            sslContext.init(null, (Build.VERSION_CODES.KITKAT == Build.VERSION.SDK_INT) ? null
                            : new TrustManager[]{x509TrustManager}
                    , /*new java.security.SecureRandom()*/null);
            return sslContext;
        } catch (Exception e) {

            e.printStackTrace();
            Timber.d(" SSL E %s", e.getMessage());
            throw new AssertionError(e);
        }
    }

    @Singleton
    @Provides
    X509TrustManager provideX509TrustManager(Context context) {

        try {
            TrustManagerFactory originalTrustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            originalTrustManagerFactory.init((KeyStore) null);
            KeyStore trustedKs = KeyStore.getInstance(KeyStore.getDefaultType());
            //TODO: Create & Refer BKS file
            try (InputStream in = context.getResources().openRawResource(/*R.raw.clienttruststore*/)) {
                trustedKs.load(in, TRUST_KEY.toCharArray());
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            originalTrustManagerFactory.init(trustedKs);
            TrustManager[] trustManagers = originalTrustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }

            return (X509TrustManager) trustManagers[0];
        } catch (Exception e) {

            e.printStackTrace();
            throw new AssertionError(e);

        }

    }

    @Singleton
    @Provides
    AuthApiService provideAuthApiService(Retrofit.Builder builder
            , @OkHttpClientAuth OkHttpClient okHttpClient) {
        return builder.client(okHttpClient).build().create(AuthApiService.class);
    }


    @Singleton
    @Provides
    ResourceApiService provideResourceApiService(Retrofit.Builder builder
            , @OkHttpClientResources OkHttpClient okHttpClient) {
        return builder.client(okHttpClient).build().create(ResourceApiService.class);
    }


}
