package com.stargatex.lahiru.basicpdfviwer.di.module;

import android.content.Context;


import com.stargatex.lahiru.basicpdfviwer.base.BaseApplication;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
@Module
public class AppContextModule {

    @Provides
    Context provideContext(BaseApplication baseApplication) {
        return baseApplication.getApplicationContext();
    }


}
