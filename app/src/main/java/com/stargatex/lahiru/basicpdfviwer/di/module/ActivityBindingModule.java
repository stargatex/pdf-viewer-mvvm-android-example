package com.stargatex.lahiru.basicpdfviwer.di.module;



import com.stargatex.lahiru.basicpdfviwer.ui.login.LoginActivity;
import com.stargatex.lahiru.basicpdfviwer.ui.viewer.PdfViewerActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector()
    abstract PdfViewerActivity bindPdfViewerActivity();

}
