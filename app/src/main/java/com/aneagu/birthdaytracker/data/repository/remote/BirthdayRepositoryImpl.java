package com.aneagu.birthdaytracker.data.repository.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aneagu.birthdaytracker.data.repository.local.BirthdayDao;
import com.aneagu.birthdaytracker.data.repository.models.Birthday;
import com.aneagu.birthdaytracker.data.repository.models.BirthdayDto;
import com.aneagu.birthdaytracker.data.repository.models.Mapper;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

//NOTE: addValueEventListener -> se apeleaza de fiecare data cand se schimba baza de date
public class BirthdayRepositoryImpl implements BirthdayRepository {

    private DatabaseReference databaseReference;

    public BirthdayRepositoryImpl(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    @Override
    public void synchronizeData(BirthdayDao daoReference, List<Birthday> localBirthdays, String currentMail) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BirthdayDto> birthdayDtoList = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    BirthdayDto dto = child.getValue(BirthdayDto.class);
                    if (dto != null) {
                        if (dto.getAuthenticatedUserEmail().equals(currentMail)) {
                            birthdayDtoList.add(dto);
                        }
                    }
                }

                syncUp(daoReference, localBirthdays, birthdayDtoList, currentMail);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    private void syncDown(List<BirthdayDto> birthdayDtoList, List<Birthday> localBirthdays, BirthdayDao dao, String currentEmail) {
        List<Birthday> birthdays = birthdayDtoList.stream()
                .map(Mapper::convertFromDtoToDao)
                .collect(Collectors.toList());

        birthdays.forEach(remoteBirthday -> {
            if (localBirthdays.size() == 0) {
                dao.save(remoteBirthday);
            } else {
                boolean isFound = false;
                for (Birthday localBirthday : localBirthdays) {
                    if (remoteBirthday.getFullName().trim().equalsIgnoreCase(localBirthday.getFullName().trim())
                            && remoteBirthday.getAuthenticatedEmail().equalsIgnoreCase(currentEmail)
                            && remoteBirthday.getDeletedOn() == null) {
                        isFound = true;
                    }
                }
                if (!isFound) {
                    dao.save(remoteBirthday);
                }
            }
        });
    }

    private void syncUp(BirthdayDao daoReference, List<Birthday> localBirthdays, List<BirthdayDto> birthdayDtoList, String currentMail) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                localBirthdays.stream()
                        .map(localBirthday -> Mapper.convertFromDaoToDto(localBirthday, currentMail))
                        .forEach(localBirthdayDto -> {
                            if (dataSnapshot.getChildrenCount() == 0) {
                                saveBirthday(localBirthdayDto);
                                return;
                            }

                            Iterable<DataSnapshot> childrenIterable = dataSnapshot.getChildren();
                            AtomicBoolean isNewBirthday = new AtomicBoolean(true);

                            updateBirthday(childrenIterable, isNewBirthday, currentMail, localBirthdayDto);
                            if (isNewBirthday.get() && localBirthdayDto.getAuthenticatedUserEmail().equals(currentMail)) {
                                saveBirthday(localBirthdayDto);
                            }

                            if (localBirthdayDto.getDeletedOn() != null) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    deleteBirthday(localBirthdayDto, child, currentMail, daoReference);
                                }
                            }
                        });
                syncDown(birthdayDtoList, localBirthdays, daoReference, currentMail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    private void deleteBirthday(BirthdayDto localBirthdayDto, DataSnapshot child, String currentMail, BirthdayDao daoReference) {
        BirthdayDto dto = child.getValue(BirthdayDto.class);
        if (dto != null) {
            if (dto.getAuthenticatedUserEmail().equals(currentMail)
                    && dto.getFullName().equalsIgnoreCase(localBirthdayDto.getFullName())) {
                child.getRef().removeValue();
                daoReference.delete(Mapper.convertFromDtoToDao(localBirthdayDto).getFullName());
            }
        }
    }

    private void saveBirthday(BirthdayDto birthdayDto) {
        DatabaseReference reference = databaseReference.push();
        reference.setValue(birthdayDto);
    }

    private void updateBirthday(Iterable<DataSnapshot> childrenIterable, AtomicBoolean isNewBirthday, String currentMail, BirthdayDto birthdayDto) {
        childrenIterable.forEach(childSnapShot -> {
            BirthdayDto dto = childSnapShot.getValue(BirthdayDto.class);
            if (dto != null) {
                if (dto.getAuthenticatedUserEmail().equals(currentMail)
                        && dto.getFullName().equalsIgnoreCase(birthdayDto.getFullName())) {
                    childSnapShot.getRef().setValue(birthdayDto);
                    isNewBirthday.set(false);
                }
            }
        });
    }
}
