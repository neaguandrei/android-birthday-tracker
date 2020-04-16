package com.aneagu.birthdaytracker.ui.birthdays;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aneagu.birthdaytracker.R;
import com.aneagu.birthdaytracker.data.repository.models.Birthday;
import com.aneagu.birthdaytracker.utils.DateUtils;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.ParseException;
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

        boolean isDefault = true;
        if (birthday.getPictureLocal() != null) {
            Bitmap bitmap = getBitmap(holder, birthday);
            if (bitmap != null) {
                isDefault = false;
                holder.picture.setImageBitmap(bitmap);
            }
        }

        if (isDefault) {
            holder.letter.setVisibility(View.VISIBLE);
            String text = String.valueOf(birthday.getFullName().toUpperCase().charAt(0));
            holder.letter.setText(text);
        }

        holder.name.setText(birthday.getFullName());
        holder.daysUntil.setText(DateUtils.findDaysLeft(birthday));
        if (birthday.getPhNumber() != null) {
            holder.phoneNumber.setText(birthday.getPhNumber());
        } else {
            holder.phoneNumber.setVisibility(View.GONE);
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
        int flags = android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_NO_YEAR;
        Date date = new Date();
        try {
            date = DateUtils.simpleDateFormat.parse(birthday.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String modifiedDate = android.text.format.DateUtils.formatDateTime(holder.date.getContext(), date.getTime(), flags);
        holder.date.setText(modifiedDate);
    }

    Birthday getItemAt(int position) {
        return dataSet.get(position);
    }

    public void deleteItem(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
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

        private TextView phoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.birthdate);
            daysUntil = itemView.findViewById(R.id.time_left);
            letter = itemView.findViewById(R.id.letter);
            phoneNumber = itemView.findViewById(R.id.phone_number);
        }
    }
}
