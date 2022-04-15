package com.stargatex.lahiru.basicpdfviwer.di.module.network;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;


public class RetrofitException extends RuntimeException {
    private static final String TAG = RetrofitException.class.getSimpleName();
    private final String url;
    private final Response response;
    private final Kind kind;
    private final Retrofit retrofit;
    private final int code;

    private RetrofitException(String message, String url, Response response, Kind kind, Throwable exception, Retrofit retrofit, int code) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
        this.retrofit = retrofit;
        this.code = code;
    }

    public static RetrofitException httpError(String url, Response response, Retrofit retrofit) {
        String message = response.code() + " " + response.message();
        Timber.d("httpError  %s %s  %s "
                , response.code(), response.message(), response.toString());
        return new RetrofitException(message, url, response, Kind.HTTP, null, retrofit, response.code());
    }

    public static RetrofitException networkError(IOException exception) {
        Timber.d("networkError ");
        return new RetrofitException(exception.getMessage(), null, null, Kind.NETWORK, exception, null, 0);
    }

    public static RetrofitException unexpectedError(Throwable exception) {
        Timber.d("unexpectedError ");
        return new RetrofitException(exception.getMessage(), null, null, Kind.UNEXPECTED, exception, null, 0);
    }

    /**
     * The request URL which produced the error.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Response object containing status code, headers, body, etc.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * The event kind which triggered this error.
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * The Retrofit this request was executed on
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    public <T> T getErrorBodyAs(Class<T> type) throws IOException {
        if (response == null || response.errorBody() == null) {
            return null;
        }
        Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(type, new Annotation[0]);

        return converter.convert(response.errorBody());
    }

    public int getCode() {
        return code;
    }

    /**
     * Identifies the event kind which triggered a {@link RetrofitException}.
     */
    public enum Kind {
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }
}
