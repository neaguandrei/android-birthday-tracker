package com.aneagu.birthdaytracker.utils;

public class ImageSendingWrapper {
    private String currentPhotoPath;
    private int targetH;
    private int targetW;

    public ImageSendingWrapper(String currentPhotoPath, int targetH, int targetW) {
        this.currentPhotoPath = currentPhotoPath;
        this.targetH = targetH;
        this.targetW = targetW;
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public int getTargetH() {
        return targetH;
    }

    public int getTargetW() {
        return targetW;
    }
}
