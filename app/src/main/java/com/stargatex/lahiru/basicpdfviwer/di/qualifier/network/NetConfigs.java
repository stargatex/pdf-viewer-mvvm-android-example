package com.stargatex.lahiru.basicpdfviwer.di.qualifier.network;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Lahiru Jayawickrama (LahiruJaya)
 * @version 1.0
 */
@IntDef({NetConfigs.CONNECT_TIMEOUT, NetConfigs.MAX_IDLE_CONNECTIONS
        , NetConfigs.KEEP_ALIVE_DURATION_MS})
@StringDef({NetConfigs.SERVICE_SUCCESS, NetConfigs.SERVICE_FAILS})
@Retention(RetentionPolicy.RUNTIME)
public @interface NetConfigs {
    int CONNECT_TIMEOUT =500000; // second
    int READ_TIMEOUT =500000;
    int MAX_IDLE_CONNECTIONS = 60;
    int MAX_RETRY_COUNT = 3;
    int DELAY_TIME = 2;// second
    int KEEP_ALIVE_DURATION_MS = 3 * 60 * 1000;
    String HTTP_PROTOCOL= "TLSv1.2";

    String SERVICE_SUCCESS= "200";
    String SERVICE_FAILS= "999";


}
