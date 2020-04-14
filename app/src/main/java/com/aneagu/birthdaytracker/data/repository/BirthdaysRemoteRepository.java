package com.aneagu.birthdaytracker.data.repository;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aneagu.birthdaytracker.data.models.Birthday;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BirthdaysRemoteRepository implements IBirthdaysRemoteRepository {

    private DatabaseReference databaseReference;

    public BirthdaysRemoteRepository(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    @Override
    public void createBirthday(Birthday birthday, String currentMail) {
        databaseReference.child(currentMail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, List<Birthday>> storage = new HashMap<>();
                List<Birthday> freshBirthdayList = new ArrayList<>();
                freshBirthdayList.add(birthday);
                storage.put(currentMail, freshBirthdayList);

                if (dataSnapshot.getChildrenCount() == 0) {
                    databaseReference.setValue(storage);
                } else {
                    dataSnapshot.getChildren().forEach(child -> {
                        Birthday current = child.getValue(Birthday.class);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public CompletableFuture getBirthdays() {
        return null;
    }

    @Override
    public CompletableFuture updateBirthday(Birthday birthday) {
        return null;
    }

    @Override
    public CompletableFuture deleteBirthday(Birthday birthday) {
        return null;
    }
}
