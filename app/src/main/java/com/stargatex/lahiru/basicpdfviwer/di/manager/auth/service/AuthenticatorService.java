package com.stargatex.lahiru.basicpdfviwer.di.manager.auth.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;


import com.stargatex.lahiru.basicpdfviwer.di.manager.auth.Authenticator;

import javax.inject.Inject;

import dagger.android.DaggerService;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public class AuthenticatorService extends DaggerService {

    @Inject
    Authenticator authenticator;

    @Override
    public void onCreate() {
        authenticator = new Authenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
