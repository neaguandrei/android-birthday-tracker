package com.aneagu.birthdaytracker.utils;

public interface AsyncTaskListener<T> {
    void onPreExecute();
    void onPostExecute(T result);
}
