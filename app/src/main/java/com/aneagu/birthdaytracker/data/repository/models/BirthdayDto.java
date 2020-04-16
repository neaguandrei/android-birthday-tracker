package com.aneagu.birthdaytracker.data.repository.models;

public class BirthdayDto {

    private String authenticatedUserEmail;

    private String fullName;

    private String date;

    private String phNumber;

    private String deletedOn;

    private String updatedOn;

    public BirthdayDto() {
    }

    public BirthdayDto(String authenticatedUserEmail, String fullName, String date, String phNumber, String deletedOn, String updatedOn) {
        this.authenticatedUserEmail = authenticatedUserEmail;
        this.fullName = fullName;
        this.date = date;
        this.phNumber = phNumber;
        this.deletedOn = deletedOn;
        this.updatedOn = updatedOn;
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

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
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

    public String getAuthenticatedUserEmail() {
        return authenticatedUserEmail;
    }

    public void setAuthenticatedUserEmail(String authenticatedUserEmail) {
        this.authenticatedUserEmail = authenticatedUserEmail;
    }
}
