package com.aneagu.birthdaytracker.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class ImageSendingAsync extends AsyncTask<ImageSendingWrapper, Void, Bitmap> {
    private WeakReference<AsyncTaskListener<Bitmap>> listenerReference = new WeakReference<>(null);

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(listenerReference.get() != null){
            listenerReference.get().onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(listenerReference.get() != null){
            listenerReference.get().onPostExecute(bitmap);
        }
    }




    @Override
    protected Bitmap doInBackground(ImageSendingWrapper... imageSendingWrappers) {
        ImageSendingWrapper wrapper = imageSendingWrappers[0];
        return loadBitmapDecodeFile(wrapper.getCurrentPhotoPath(), wrapper.getTargetW(), wrapper.getTargetH());
    }

    private Bitmap loadBitmapDecodeFile(String currentPhotoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int heightRatio = (int) Math.ceil(bmOptions.outHeight / (float) targetH);
        int widthRatio = (int) Math.ceil(bmOptions.outWidth / (float) targetW);
        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmOptions.inSampleSize = heightRatio;
            } else {
                bmOptions.inSampleSize = widthRatio;
            }
        }

        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

    }



    public void setListenerReference(AsyncTaskListener<Bitmap> listenerReference) {
        this.listenerReference = new WeakReference<>(listenerReference);
    }
}
