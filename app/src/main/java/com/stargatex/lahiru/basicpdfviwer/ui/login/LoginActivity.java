package com.stargatex.lahiru.basicpdfviwer.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.stargatex.lahiru.basicpdfviwer.R;
import com.stargatex.lahiru.basicpdfviwer.base.BaseBindingActivity;
import com.stargatex.lahiru.basicpdfviwer.databinding.ActivityLoginBinding;
import com.stargatex.lahiru.basicpdfviwer.di.module.view.ViewModelFactory;
import com.stargatex.lahiru.basicpdfviwer.ui.viewer.PdfViewerActivity;

import javax.inject.Inject;

public class LoginActivity extends BaseBindingActivity<ActivityLoginBinding> {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected int layoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected ViewModel getViewModel() {
        return loginViewModel = new ViewModelProvider(this, viewModelFactory)
                .get(LoginViewModel.class);
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = getViewDataBinding();

        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.login;
        loadingProgressBar = binding.loading;

        observables();
        setup();

    }

    private void setup() {
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void observables() {
        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, response -> {
            loadingProgressBar.setVisibility(View.GONE);
            if (response == null) return;

            switch (response.status) {
                case LOADING:
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    Intent intent = new Intent(LoginActivity.this, PdfViewerActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case ERROR:
                    showSnack(response.message);
                    break;
            }
        });
    }

}