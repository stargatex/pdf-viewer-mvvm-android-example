package com.stargatex.lahiru.basicpdfviwer.di.component;

import android.app.Application;

import com.stargatex.lahiru.basicpdfviwer.base.BaseApplication;
import com.stargatex.lahiru.basicpdfviwer.di.module.ActivityBindingModule;
import com.stargatex.lahiru.basicpdfviwer.di.module.ContextModule;
import com.stargatex.lahiru.basicpdfviwer.di.module.DateUtilsModule;
import com.stargatex.lahiru.basicpdfviwer.di.module.NetworkModule;
import com.stargatex.lahiru.basicpdfviwer.di.module.system.SystemServiceModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;

/**
 * @author LahiruJaya
 */
@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityBindingModule.class
        , SystemServiceModule.class, NetworkModule.class, ContextModule.class, DateUtilsModule.class})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication>, AppUtilComponent {
    void inject(BaseApplication baseApplication);


    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }
}
