package com.stargatex.lahiru.basicpdfviwer.data.repository;



import com.stargatex.lahiru.basicpdfviwer.data.service.ResourceApiService;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author Lahiru Jayawickrama (LahiruJaya)
 * @version 1.0
 */
public class ResourceApiRepository {
    private final ResourceApiService resourceApiService;
    @Inject
    public ResourceApiRepository(ResourceApiService resourceApiService) {
        this.resourceApiService = resourceApiService;
    }

    public Maybe<Response<ResponseBody>> retrievePdf(String resourcePath){
        return resourceApiService.retrievePdf(resourcePath);
    }

}
