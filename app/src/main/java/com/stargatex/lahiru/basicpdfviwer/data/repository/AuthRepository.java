package com.stargatex.lahiru.basicpdfviwer.data.repository;

import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.app.AccManagerDef.GRANT_TYPE;
import static com.stargatex.lahiru.basicpdfviwer.di.qualifier.app.AccManagerDef.SCOPE;


import com.stargatex.lahiru.basicpdfviwer.data.model.LoginResponse;
import com.stargatex.lahiru.basicpdfviwer.data.service.AuthApiService;

import javax.inject.Inject;

import io.reactivex.Single;

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public class AuthRepository {
    private AuthApiService authApiService;

    @Inject
    public AuthRepository(AuthApiService authApiService) {
        this.authApiService = authApiService;
    }
    public Single<LoginResponse> getLogin(String username, String password) {
        return authApiService.login(GRANT_TYPE, username, password, SCOPE);
    }

}
