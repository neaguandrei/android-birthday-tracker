package com.aneagu.birthdaytracker.data.repository.remote;

import com.aneagu.birthdaytracker.data.repository.local.BirthdayDao;
import com.aneagu.birthdaytracker.data.repository.models.Birthday;

import java.util.List;

public interface BirthdayRepository {

    void synchronizeData(BirthdayDao daoReference, List<Birthday> localBirthdays, String currentMail);
}
