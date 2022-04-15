package com.stargatex.lahiru.basicpdfviwer.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.stargatex.lahiru.basicpdfviwer.di.module.network.RetrofitException;

import org.jetbrains.annotations.NotNull;

public class AppResource<T> {


    @NonNull
    public final AppStatus status;
    @Nullable
    public final T data;
    @Nullable
    public final int code;
    @Nullable
    public final String message;

    public AppResource(@NonNull AppStatus status, @Nullable T data, @Nullable int code, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public static <T> AppResource<T> success(@NonNull String msg, @Nullable T data) {
        return new AppResource<>(AppStatus.SUCCESS, data, 0, msg);
    }

    public static <T> AppResource<T> error(@NonNull String msg) {
        return new AppResource<>(AppStatus.ERROR, null, -1, msg);
    }

    public static <T> AppResource<T> error(@NonNull String msg, @NotNull Throwable e) {
        int errorCode = -1;
        try {
            RetrofitException error = (RetrofitException) e;
            if (error.getKind() != null && error.getKind().equals(RetrofitException.Kind.HTTP)) {
                errorCode = error.getCode();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new AppResource<>(AppStatus.ERROR, null, errorCode, msg);
    }

    public static <T> AppResource<T> loading() {
        return new AppResource<>(AppStatus.LOADING, null, -1, null);
    }


    public enum AppStatus {SUCCESS, ERROR, LOADING}
}
