package com.aneagu.birthdaytracker.ui.birthdays;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aneagu.birthdaytracker.R;
import com.aneagu.birthdaytracker.data.repository.local.Birthday;
import com.aneagu.birthdaytracker.data.repository.local.BirthdayDao;
import com.aneagu.birthdaytracker.data.module.AppController;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class BirthdaysFragment extends Fragment {

    public static final int REQUEST_CODE_ADD = 1;

    @Inject
    BirthdayDao birthdayDao;

    @BindView(R.id.birthdays_list)
    RecyclerView rvBirthdays;

    @BindView(R.id.searchbox)
    EditText editText;

    @OnClick(R.id.fab_add_birthday)
    void onClick() {
        Intent intent = new Intent(getActivity(), NewBirthdayActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_birthdays, container, false);
        ButterKnife.bind(this, root);
        editText.addTextChangedListener(getWatcher());
        setUpDelete();
        return root;
    }

    private void supplyData(String searchKey) {
        rvBirthdays.setHasFixedSize(true);
        rvBirthdays.setLayoutManager(new LinearLayoutManager(getContext()));
        CompletableFuture.supplyAsync(() -> birthdayDao.findAllByName(searchKey))
                .thenAccept(birthdays -> {
                    BirthdayAdapter adapter = new BirthdayAdapter(birthdays);
                    rvBirthdays.setAdapter(adapter);
                });
    }

    private void supplyData() {
        rvBirthdays.setHasFixedSize(true);
        rvBirthdays.setLayoutManager(new LinearLayoutManager(getContext()));
        CompletableFuture.supplyAsync(() -> birthdayDao.findAll())
                .thenAccept(birthdays -> {
                    BirthdayAdapter adapter = new BirthdayAdapter(birthdays);
                    rvBirthdays.setAdapter(adapter);
                });
    }

    private void setUpDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                BirthdayAdapter adapter = ((BirthdayAdapter) Objects.requireNonNull(rvBirthdays.getAdapter()));
                Birthday birthday = adapter.getItemAt(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete this " + birthday.getFullName() + "'s birthday?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            CompletableFuture.runAsync(() -> birthdayDao.delete(birthday))
                                    .thenAccept(aVoid -> adapter.deleteItem(position));
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> {
                            adapter.notifyDataSetChanged();
                        })
                        .show();
            }
        }).attachToRecyclerView(rvBirthdays);
    }


    @NotNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                supplyData(editable.toString());
            }
        };
    }

    @Override
    public void onAttach(@NotNull Context context) {
        ((AppController) context.getApplicationContext()).getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        supplyData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_CANCELED || Objects.isNull(data)) {
            return;
        }

        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            Toast.makeText(getContext(), "Birthday added successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
