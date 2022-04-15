package com.stargatex.lahiru.basicpdfviwer.di.module.system;


import android.accounts.AccountManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
@Module
public class SystemServiceModule {

    @Singleton
    @Provides
    AccountManager provideAccountManager(Context context) {
        return AccountManager.get(context);
    }

}
