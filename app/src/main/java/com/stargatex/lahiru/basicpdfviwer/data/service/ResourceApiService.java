package com.stargatex.lahiru.basicpdfviwer.data.service;



import static com.stargatex.lahiru.basicpdfviwer.utils.constant.NetBaseConstants.PROTECTED_PDF_PATH;

import io.reactivex.Maybe;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * @author Lahiru Jayawickrama (LahiruJaya)
 * @version 1.0
 */
public interface ResourceApiService {

   @Headers("Content-Type: application/pdf")
   @Streaming
   @GET(PROTECTED_PDF_PATH)
   Maybe<Response<ResponseBody>> retrievePdf(@Path(value = "path", encoded = true) String path);

}
