package com.stargatex.lahiru.basicpdfviwer.di.module;

import androidx.lifecycle.ViewModel;


import com.stargatex.lahiru.basicpdfviwer.di.utils.ViewModelKey;
import com.stargatex.lahiru.basicpdfviwer.ui.login.LoginViewModel;
import com.stargatex.lahiru.basicpdfviwer.ui.viewer.PdfViewerViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(PdfViewerViewModel.class)
    abstract ViewModel bindPdfViewerViewModel(PdfViewerViewModel pdfViewerViewModel);

}
