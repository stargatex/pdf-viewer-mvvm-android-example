package com.stargatex.lahiru.basicpdfviwer.data.service;

import static com.stargatex.lahiru.basicpdfviwer.utils.constant.NetBaseConstants.APIM_TOKEN_PATH;

import com.stargatex.lahiru.basicpdfviwer.data.model.LoginResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public interface AuthApiService {
    @FormUrlEncoded
    @POST(APIM_TOKEN_PATH)
    Single<LoginResponse> login(@Field("grant_type") String grant_type,
                                @Field("username") String username,
                                @Field("password") String password,
                                @Field("scope") String scope);

    @FormUrlEncoded
    @POST(APIM_TOKEN_PATH)
    Single<LoginResponse> getTokenUsingRefreshToken(@Field("grant_type") String grant_type,
                                                   @Field("refresh_token") String refresh_token);
}
