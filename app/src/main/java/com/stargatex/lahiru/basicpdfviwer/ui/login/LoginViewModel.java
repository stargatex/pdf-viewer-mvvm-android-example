package com.stargatex.lahiru.basicpdfviwer.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stargatex.lahiru.basicpdfviwer.data.source.AppResource;
import com.stargatex.lahiru.basicpdfviwer.R;
import com.stargatex.lahiru.basicpdfviwer.data.model.LoginResponse;
import com.stargatex.lahiru.basicpdfviwer.data.repository.AuthRepository;
import com.stargatex.lahiru.basicpdfviwer.di.manager.auth.UserManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<AppResource<LoginResponse>> loginResponse = new MutableLiveData<>();
    private AuthRepository authRepository;
    private CompositeDisposable disposable;

    @Inject
    UserManager userManager;

    @Inject
    LoginViewModel(AuthRepository loginRepository) {
        this.authRepository = loginRepository;
        disposable = new CompositeDisposable();
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<AppResource<LoginResponse>> getLoginResult() {
        return loginResponse;
    }

    public void login(String username, String password) {
        loginResponse.setValue(AppResource.loading());
        disposable.add(authRepository.getLogin(username, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse login) {
                        loginResponse.postValue(AppResource.success("", login));
                        userManager.connectAccount(username, password, login);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //loginResponse.setValue(AppResource.error(netErrorHandler.errorObjectOf(e).getMessages()));
                    }
                }));
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return true;
            //return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(disposable!=null){
            disposable.clear();
            disposable = null;
        }
    }
}