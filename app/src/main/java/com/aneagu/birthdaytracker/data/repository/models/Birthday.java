package com.aneagu.birthdaytracker.data.repository.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"full_name"},
        unique = true)})
public class Birthday {

    @PrimaryKey
    private Long id;

    @ColumnInfo(name = "full_name")
    private String fullName;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "picture_local")
    private String pictureLocal;

    @ColumnInfo(name = "phone_number")
    private String phNumber;

    @ColumnInfo(name = "deleted_on")
    private String deletedOn;

    @ColumnInfo(name = "updated_on")
    private String updatedOn;

    @ColumnInfo(name = "authenticated_email")
    private String authenticatedEmail;

    @Ignore
    public Birthday() {
    }

    public Birthday(String fullName, String date, String pictureLocal, String authenticatedEmail) {
        this.fullName = fullName;
        this.date = date;
        this.pictureLocal = pictureLocal;
        this.authenticatedEmail = authenticatedEmail;
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

    public String getPictureLocal() {
        return pictureLocal;
    }

    public void setPictureLocal(String pictureLocal) {
        this.pictureLocal = pictureLocal;
    }

    public String getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(String deletedOn) {
        this.deletedOn = deletedOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getAuthenticatedEmail() {
        return authenticatedEmail;
    }

    public void setAuthenticatedEmail(String authenticatedEmail) {
        this.authenticatedEmail = authenticatedEmail;
    }


    @Override
    public String toString() {
        return fullName + "'s birthday is on " + date;
    }
}
