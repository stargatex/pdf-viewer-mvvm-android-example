package com.stargatex.lahiru.basicpdfviwer.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import dagger.android.support.DaggerFragment;


/**
 * @author LahiruJaya
 */

public abstract class BaseBindingFragment<T extends ViewDataBinding> extends DaggerFragment {
    private static final String TAG = BaseBindingFragment.class.getSimpleName();
    private AppCompatActivity activity;
    private T viewDataBinding;
    private ViewModel viewModel;
    private Bundle bundleArgs;
    private Dialog mProgress;

    @LayoutRes
    protected abstract int layoutRes();

    protected abstract ViewModel getViewModel();

    protected abstract int getBindingVariable();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = (viewModel == null) ? getViewModel() : viewModel;
        bundleArgs = getArguments();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutRes(), container, false);
        View view = viewDataBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewDataBinding.setVariable(getBindingVariable(), viewModel);
        viewDataBinding.setLifecycleOwner(this);
        viewDataBinding.executePendingBindings();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    protected T getViewDataBinding() {
        return viewDataBinding;
    }

    protected AppCompatActivity getBaseActivity() {
        return activity;
    }

    protected Bundle getBundleArgs() {
        return bundleArgs;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


}
