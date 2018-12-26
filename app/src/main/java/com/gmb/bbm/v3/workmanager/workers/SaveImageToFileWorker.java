package com.gmb.bbm.v3.workmanager.workers;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveImageToFileWorker extends Worker {

    public SaveImageToFileWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    private static final String TAG = SaveImageToFileWorker.class.getSimpleName();

    private static final String TITLE = "Blurred Image";
    private static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault());

    @NonNull
    @Override
    public Worker.Result doWork() {
        Context applicationContext = getApplicationContext();

        ContentResolver resolver = applicationContext.getContentResolver();
        try {

            WorkerUtils.makeStatusNotification("Doing <WORK SaveImageToFile>", applicationContext);
            WorkerUtils.sleep();

            String resourceUri =""; // TODO get the input Uri from the Data object
                    Bitmap bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri)));
            String imageUrl = MediaStore.Images.Media.insertImage(
                    resolver, bitmap, TITLE, DATE_FORMATTER.format(new Date()));
            if (TextUtils.isEmpty(imageUrl)) {
                Log.e(TAG, "Writing to MediaStore failed");
                return Worker.Result.failure();
            }
            // TODO create and set the output Data object with the imageUri.
            return Worker.Result.success();
        } catch (Exception exception) {
            Log.e(TAG, "Unable to save image to Gallery", exception);
            return Worker.Result.failure();
        }
    }
}
