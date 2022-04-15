package com.stargatex.lahiru.basicpdfviwer.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * @author LahiruJaya
 */

public abstract class BaseBindingActivity<T extends ViewDataBinding> extends DaggerAppCompatActivity {

    private T viewDataBinding;
    private ViewModel viewModel;


    @LayoutRes
    protected abstract int layoutRes();

    protected abstract ViewModel getViewModel();

    protected abstract int getBindingVariable();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, layoutRes());
        this.viewModel = (viewModel == null) ? getViewModel() : viewModel;
        viewDataBinding.setVariable(getBindingVariable(), viewModel);
        viewDataBinding.executePendingBindings();

    }

    protected T getViewDataBinding() {
        return viewDataBinding;
    }

    protected void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSnack(String message) {
        if (message != null) {
            hideKeyboard(this);
            Snackbar.make(viewDataBinding.getRoot(), message, BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }


}

