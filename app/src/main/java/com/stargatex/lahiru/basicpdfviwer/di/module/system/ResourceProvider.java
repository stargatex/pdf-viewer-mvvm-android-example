package com.stargatex.lahiru.basicpdfviwer.di.module.system;

import android.content.Context;

import javax.inject.Inject;

/**
 * @author Lahiru Jayawickrama (LahiruJaya)
 * @version 1.0
 */

public class ResourceProvider {
    private final Context context;

    @Inject
    public ResourceProvider(Context context) {
        this.context = context;
    }

    public String getString(int resId) {
        return context.getString(resId);
    }

    public String getString(int resId, String value) {
        return context.getString(resId, value);
    }
}
