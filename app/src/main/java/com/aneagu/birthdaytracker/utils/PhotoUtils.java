package com.aneagu.birthdaytracker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

public class PhotoUtils {

    private static String getImagePath(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
    }

    public static Uri getImageUri(Context context, Bitmap inImage) {
        return Uri.parse(getImagePath(context, inImage));
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

}
