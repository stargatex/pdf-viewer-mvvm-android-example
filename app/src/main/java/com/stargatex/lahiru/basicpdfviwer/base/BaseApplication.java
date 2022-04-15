package com.stargatex.lahiru.basicpdfviwer.base;

import android.content.Context;

import androidx.multidex.MultiDex;


import com.stargatex.lahiru.basicpdfviwer.BuildConfig;
import com.stargatex.lahiru.basicpdfviwer.di.component.ApplicationComponent;

import com.stargatex.lahiru.basicpdfviwer.di.component.DaggerApplicationComponent;
import com.stargatex.lahiru.basicpdfviwer.di.module.system.AppInformation;
import com.stargatex.lahiru.basicpdfviwer.utils.dev.CustomDebugTree;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import timber.log.Timber;

/**
 * @author LahiruJaya
 */
public class BaseApplication extends DaggerApplication {
    private ApplicationComponent component;
    @Inject
    AppInformation appInformation;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        component = DaggerApplicationComponent.builder().application(this).build();
        component.inject(this);
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new CustomDebugTree());

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }




}
