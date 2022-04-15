package com.stargatex.lahiru.basicpdfviwer.ui.viewer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stargatex.lahiru.basicpdfviwer.data.repository.ResourceApiRepository;
import com.stargatex.lahiru.basicpdfviwer.data.repository.SystemResourceRepository;
import com.stargatex.lahiru.basicpdfviwer.data.source.AppResource;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public class PdfViewerViewModel extends ViewModel {

    private final ResourceApiRepository resourceApiRepository;
    private final SystemResourceRepository systemResourceRepository;
    private final MutableLiveData<AppResource<byte[]>> pdfFileByte = new MutableLiveData<>();
    private CompositeDisposable disposable;

    @Inject
    public PdfViewerViewModel(ResourceApiRepository resourceApiRepository
            , SystemResourceRepository systemResourceRepository) {
        this.resourceApiRepository = resourceApiRepository;
        this.systemResourceRepository = systemResourceRepository;
        disposable = new CompositeDisposable();
    }

    public void loadCribFile(String origin) {

        disposable.add(resourceApiRepository.retrievePdf(origin)
                .flatMap((Function<Response<ResponseBody>, MaybeSource<byte[]>>) responseBodyResponse -> {

                    if (responseBodyResponse != null && responseBodyResponse.code() == 200
                            && responseBodyResponse.body() != null) {
                        return systemResourceRepository
                                .getBytesFromInputStream(responseBodyResponse.body().byteStream());
                    }
                    return Maybe.empty();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pdfByte -> {
                    Timber.d(" pdf length %s", pdfByte.length);
                    pdfFileByte.postValue(AppResource.success("", pdfByte));
                }, throwable -> {
                    Timber.d(" pdf %s", throwable.getMessage());
                    throwable.printStackTrace();

                }));

    }

    public LiveData<AppResource<byte[]>> getPdfFileByte() {
        return pdfFileByte;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }
}
