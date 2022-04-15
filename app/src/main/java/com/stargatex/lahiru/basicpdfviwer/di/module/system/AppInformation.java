package com.stargatex.lahiru.basicpdfviwer.di.module.system;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
@Singleton
public class AppInformation {
    private final Context context;

    @Inject
    public AppInformation(Context context) {
        this.context = context;
    }

    public String getAppVersionNameCleaned() {
        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);

        }

        return result;
    }

    public String getAppVersionName() {
        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);

        }
        return result;
    }

    public long getAppVersionCode() {
        long result = 1;

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);

        }

        return result;
    }

    public String getDeviceName() {
        //String model = Build.MODEL;
        return Build.MANUFACTURER;
    }


}
