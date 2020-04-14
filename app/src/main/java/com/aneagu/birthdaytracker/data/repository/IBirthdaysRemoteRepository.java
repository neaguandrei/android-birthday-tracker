package com.aneagu.birthdaytracker.data.repository;

import com.aneagu.birthdaytracker.data.models.Birthday;

import java.util.concurrent.CompletableFuture;

public interface IBirthdaysRemoteRepository {

    void createBirthday(Birthday birthday, String currentMail);

    CompletableFuture getBirthdays();

    CompletableFuture updateBirthday(Birthday birthday);

    CompletableFuture deleteBirthday(Birthday birthday);
}
