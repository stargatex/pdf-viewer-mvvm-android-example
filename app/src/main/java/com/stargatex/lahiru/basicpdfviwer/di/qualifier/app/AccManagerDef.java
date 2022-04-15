package com.stargatex.lahiru.basicpdfviwer.di.qualifier.app;



import com.stargatex.lahiru.basicpdfviwer.BuildConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Lahiru Jayawickrama (LahiruJaya)
 * @version 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface AccManagerDef {
    String ACCOUNT_TYPE = BuildConfig.APPLICATION_ID+".di.module.auth";
    String ACCOUNT = "PdfViewerMT"+BuildConfig.FLAVOR;
    String USER_STORE = "";
    String KEY_REFRESH_TOKEN = "refresh_token";
    String KEY_EXPIRE_TIME = "expires_in";
    String ARG_ACCOUNT_TYPE = "accountType";
    String ARG_AUTH_TOKEN_TYPE = "authTokenType";
    String KEY_FEATURES = "features";
    String KEY_CREATE_ACCOUNT = "createAccount";
    String GRANT_TYPE = "password";
    String SCOPE = "SCOPE";
    String DEFAULT_AUTH_TOKEN_TYPE = "defaultAuthTokenType";
    String KEY_USERNAME = "username";
}
