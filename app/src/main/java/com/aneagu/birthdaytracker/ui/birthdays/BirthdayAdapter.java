package com.aneagu.birthdaytracker.ui.birthdays;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aneagu.birthdaytracker.R;
import com.aneagu.birthdaytracker.data.repository.local.Birthday;
import com.aneagu.birthdaytracker.utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.ViewHolder> {

    private List<Birthday> dataSet;

    public BirthdayAdapter(List<Birthday> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_birthdays, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Birthday birthday = dataSet.get(position);

        modifyDate(holder, birthday);

        if (birthday.getPictureLocal() != null) {
            Bitmap bitmap = getBitmap(holder, birthday);
            if (bitmap != null) {
                holder.picture.setImageBitmap(bitmap);
            }
        }

        holder.name.setText(birthday.getFullName());
        holder.daysUntil.setText(findDaysLeft(birthday));
        if (birthday.getPictureLocal() == null) {
            holder.letter.setVisibility(View.VISIBLE);
            String text = String.valueOf(birthday.getFullName().toUpperCase().charAt(0));
            holder.letter.setText(text);
        }
    }

    @Nullable
    private Bitmap getBitmap(@NonNull ViewHolder holder, Birthday birthday) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(holder.picture.getContext().getApplicationContext().getContentResolver()
                    , Uri.parse(birthday.getPictureLocal()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void modifyDate(@NonNull ViewHolder holder, Birthday birthday) {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;
        Date date = new Date();
        try {
            date = Utils.simpleDateFormat.parse(birthday.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String modifiedDate = DateUtils.formatDateTime(holder.date.getContext(), date.getTime(), flags);
        holder.date.setText(modifiedDate);
    }


    private String findDaysLeft(Birthday birthday) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date birthDate = Utils.fromStringToDate(birthday.getDate());

        LocalDate fromDate = LocalDate.now();
        LocalDate untilDate = birthDate.toInstant().atZone(defaultZoneId).toLocalDate().withYear(fromDate.getYear());

        long daysNumber = Duration.between(fromDate.atStartOfDay(), untilDate.atStartOfDay()).toDays();
        if (untilDate.isBefore(fromDate)) {
            daysNumber = fromDate.lengthOfYear() + daysNumber;
        }

        if (daysNumber == 0) {
            return "Today";
        } else if (daysNumber == 1) {
            return "Tomorrow";
        }

        return "In " + daysNumber + " days";
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView picture;

        private TextView name;

        private TextView date;

        private TextView daysUntil;

        private TextView letter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.birthdate);
            daysUntil = itemView.findViewById(R.id.time_left);
            letter = itemView.findViewById(R.id.letter);
        }
    }
}
