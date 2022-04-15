package com.stargatex.lahiru.basicpdfviwer.data.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import timber.log.Timber;

/**
 * @author Lahiru Jayawickrama (LahiruJaya)
 * @version 1.0
 */
public class SystemResourceRepository {
    Context context;

    @Inject
    public SystemResourceRepository(Context context) {
        this.context= context;
    }


    public Maybe<byte[]> getBytesFromInputStream(InputStream inputStream) throws IOException {
        byte[] buffer;
        try {
            buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return Maybe.just(output.toByteArray());
        } catch (OutOfMemoryError e) {
            return Maybe.error(e);
        } catch (Exception e){
            return Maybe.error(e);
        }
    }

}
