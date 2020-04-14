package com.aneagu.birthdaytracker.data.repository.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Birthday {

    @PrimaryKey
    private Long id;

    @ColumnInfo(name = "full_name")
    private String fullName;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "picture_url")
    private String pictureUrl;

    @ColumnInfo(name = "picture_local")
    private String pictureLocal;

    public Birthday(String fullName, String date, String pictureUrl, String pictureLocal) {
        this.fullName = fullName;
        this.date = date;
        this.pictureUrl = pictureUrl;
        this.pictureLocal = pictureLocal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureLocal() {
        return pictureLocal;
    }

    public void setPictureLocal(String pictureLocal) {
        this.pictureLocal = pictureLocal;
    }
}
