package com.stargatex.lahiru.basicpdfviwer.ui.viewer;

import android.os.Bundle;
import android.view.MotionEvent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.stargatex.lahiru.basicpdfviwer.R;
import com.stargatex.lahiru.basicpdfviwer.base.BaseBindingActivity;
import com.stargatex.lahiru.basicpdfviwer.databinding.ActivityPdfViewerBinding;
import com.stargatex.lahiru.basicpdfviwer.di.module.view.ViewModelFactory;

import javax.inject.Inject;

import timber.log.Timber;

public class PdfViewerActivity extends BaseBindingActivity<ActivityPdfViewerBinding> {

    @Inject
    ViewModelFactory viewModelFactory;
    private PdfViewerViewModel pdfViewerViewModel;
    private ActivityPdfViewerBinding binding;

    @Override
    protected int layoutRes() {
        return R.layout.activity_pdf_viewer;
    }

    @Override
    protected ViewModel getViewModel() {
        return pdfViewerViewModel = new ViewModelProvider(this, viewModelFactory)
                .get(PdfViewerViewModel.class);
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        observable();
        pdfViewerViewModel.loadCribFile("");
    }

    private void observable() {
        pdfViewerViewModel.getPdfFileByte().observe(this, response -> {
            //TODO: progress bar dismiss
            if (response == null) return;

            switch (response.status) {
                case LOADING:
                    //TODO: progress bar show
                    break;
                case SUCCESS:
                    setupPdf(response.data);
                    break;
                case ERROR:
                    showSnack(response.message);
                    break;
            }
        });
    }

    private void setupPdf(byte[] data) {
        if (data != null) {
            binding.pdfView.fromBytes(data).enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            Timber.d("loadComplete pdfView");
                        }
                    }) // called after document is loaded and starts to be rendered
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {
                            Timber.d("onError pdfView %s", t.getMessage());
                        }
                    })
                    .onPageError(new OnPageErrorListener() {
                        @Override
                        public void onPageError(int page, Throwable t) {
                            Timber.d("onPageError pdfView %s", t.getMessage());
                        }
                    })
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages) {
                            Timber.d("onInitiallyRendered pdfView %s", nbPages);
                        }
                    }) // called after document is rendered for the first time
                    // called on single tap, return true if handled, false to toggle scroll handle visibility
                    .onTap(new OnTapListener() {
                        @Override
                        public boolean onTap(MotionEvent e) {
                            Timber.d("onTap pdfView ");
                            return false;
                        }
                    })
                    .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    .spacing(5)
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                    .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                    .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(true) // snap pages to screen boundaries
                    .pageFling(true) // make a fling change only a single page like ViewPager
                    .nightMode(false) // toggle night mode
                    .autoSpacing(false)
                    .load();
        } else {
            showSnack("PDF Data Not Available");
        }
    }
}