package com.stargatex.lahiru.basicpdfviwer.utils.constant;

import android.util.Base64;

import com.stargatex.lahiru.basicpdfviwer.BuildConfig;


/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public class SecConstants {

    static {

        System.loadLibrary("native-lj-lib");

    }

    public static native String trustAccessKeyJNI();

    public static native String devServiceBaseUrlJNI();

    public static native String devOauthClientId();

    public static native String devOauthClientSecret();

    private static String getServicesBaseUrl() {
        if (BuildConfig.FLAVOR_SERVER.equals("DEV"))
            return devServiceBaseUrlJNI();
        //TODO: Provide ENV configs
        return null;

    }

    private static String getIdsOauth2ClientId() {
        if (BuildConfig.FLAVOR_SERVER.equals("DEV"))
            return devOauthClientId();
        //TODO: Provide ENV configs
        return null;
    }

    private static String getIdsOauth2ClientSecret() {
        if (BuildConfig.FLAVOR_SERVER.equals("DEV"))
            return devOauthClientSecret();
        //TODO: Provide ENV configs
        return null;
    }
    public static final String TRUST_KEY = new String(Base64.decode(trustAccessKeyJNI(), Base64.DEFAULT));
    public static final String SERVICES_BASE_URL = new String(Base64.decode(getServicesBaseUrl(), Base64.DEFAULT));
    public static final String IDS_OAUTH2_CLIENT_ID = new String(Base64.decode(getIdsOauth2ClientId(), Base64.DEFAULT));
    public static final String IDS_OAUTH2_CLIENT_SECRET = new String(Base64.decode(getIdsOauth2ClientSecret(), Base64.DEFAULT));


}
