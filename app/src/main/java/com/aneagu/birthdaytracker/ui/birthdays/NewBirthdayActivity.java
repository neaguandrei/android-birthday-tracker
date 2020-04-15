package com.aneagu.birthdaytracker.ui.birthdays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.aneagu.birthdaytracker.R;
import com.aneagu.birthdaytracker.data.repository.local.Birthday;
import com.aneagu.birthdaytracker.data.repository.local.BirthdayDao;
import com.aneagu.birthdaytracker.data.module.AppController;
import com.aneagu.birthdaytracker.utils.AsyncTaskListener;
import com.aneagu.birthdaytracker.utils.ImageSendingAsync;
import com.aneagu.birthdaytracker.utils.ImageSendingWrapper;
import com.aneagu.birthdaytracker.utils.PhotoUtils;
import com.aneagu.birthdaytracker.utils.DateUtils;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewBirthdayActivity extends AppCompatActivity {

    @BindView(R.id.add_birthday_picture)
    CircleImageView ivPicture;

    @BindView(R.id.add_birthday_name)
    TextInputEditText tieName;

    @BindView(R.id.add_birthday_date)
    TextInputEditText tieDate;

    @BindView(R.id.add_birthday_phone)
    TextInputEditText tiePhone;

    @BindView(R.id.profile_progressBar)
    ProgressBar progressBar;

    @Inject
    BirthdayDao birthdayDao;

    private static Calendar pickedDate = null;

    private static NewBirthdayActivity staticReference;

    protected static final int REQUEST_CHOOSE_PHOTO_GALLERY = 3;
    protected static final int REQUEST_TAKE_PHOTO = 2;
    private static final int REQUEST_PERMISSIONS = 12;
    private static final String STORAGE_PATH = "images/";
    private String currentPhotoPath = null;
    protected boolean hasFileAccess;
    protected boolean hasCameraAccess;
    protected Uri selectedImage;

    @OnClick(R.id.add_birthday_picture)
    void onChoosePicture() {
        takePhoto();
    }

    @OnClick(R.id.add_birthday_date)
    void onChooseDate() {
        runOnUiThread(() -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
    }

    @OnClick(R.id.btn_add_birthday)
    void onSave() {
        if (isValid()) {
            saveBirthday();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        ((AppController) getApplicationContext()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        staticReference = this;
    }

    private void saveBirthday() {
        String fullName = tieName.getText() != null ? tieName.getText().toString() : null;
        String date = tieDate.getText() != null ? tieDate.getText().toString() : null;
        String imageUri = selectedImage != null ? selectedImage.toString() : null;
        Birthday birthday = new Birthday(fullName, date, null, imageUri);
        CompletableFuture.runAsync(() -> {
            birthdayDao.save(birthday);
        }).thenAccept(aVoid -> {
            setResult(RESULT_OK, getIntent());
            finish();
        });
    }

    private void takePhoto() {
        if (!haveAllPermissions()) {
            requestMissingPermissions();
            return;
        }
        showChoosePictureDialog();
    }

    protected void showChoosePictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle(getString(R.string.select_action));
        String[] pictureDialogItems = {
                getString(R.string.select_photo_gallery),
                getString(R.string.capture_photo_camera)};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            choosePhotoFromGallery();
                            break;
                        case 1:
                            takePhotoFromCamera();
                            break;
                    }
                });
        pictureDialog.show();
    }

    protected void choosePhotoFromGallery() {
        String action;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            action = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent pickPhoto = new Intent(action,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_CHOOSE_PHOTO_GALLERY);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    protected void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.aneagu.birthdaytracker.FileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
        }
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    protected boolean haveAllPermissions() {
        updatePermissions();
        return hasFileAccess && hasCameraAccess;
    }

    private void updatePermissions() {
        hasFileAccess = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        hasCameraAccess = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    protected void requestMissingPermissions() {
        List<String> request = new ArrayList<>();
        if (!hasFileAccess) {
            request.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            request.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!hasCameraAccess) {
            request.add(Manifest.permission.CAMERA);
        }

        this.requestPermissions(
                request.toArray(new String[0]),
                REQUEST_PERMISSIONS
        );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (haveAllPermissions()) {
            showChoosePictureDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    onCameraResult();
                    break;
                case REQUEST_CHOOSE_PHOTO_GALLERY:
                    onGalleryResult(data);
                    break;
                default:
                    break;
            }
        }
    }

    private void onGalleryResult(Intent data) {
        selectedImage = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            if (bitmap != null) {
                ivPicture.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onCameraResult() {
        ImageSendingAsync asyncImageSending = new ImageSendingAsync();
        asyncImageSending.setListenerReference(new AsyncTaskListener<Bitmap>() {
            @Override
            public void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    selectedImage = PhotoUtils.getImageUri(getApplicationContext(), bitmap);
                    if (selectedImage != null) {
                        ivPicture.setImageBitmap(bitmap);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        asyncImageSending.execute(new ImageSendingWrapper(currentPhotoPath, width, height));
    }

    public String getImageExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private boolean isValid() {
        if (Objects.isNull(tieName) || tieName.getText() == null || tieName.getText().toString().isEmpty()) {
            tieName.setError("Name can't be empty!");
            tieName.requestFocus();
            return false;
        }

        if (tiePhone != null && tiePhone.getText() != null && !tiePhone.getText().toString().isEmpty()) {
            if (tiePhone.getText().toString().length() < 6) {
                tiePhone.setError("Phone should have at least 6 digits!");
                tiePhone.requestFocus();
                return false;
            }
        }

        return true;
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @NotNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (pickedDate == null) {
                pickedDate = Calendar.getInstance();
            }

            int year = pickedDate.get(Calendar.YEAR);
            int month = pickedDate.get(Calendar.MONTH);
            int day = pickedDate.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(Objects.requireNonNull(getActivity()), AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            pickedDate = Calendar.getInstance();
            pickedDate.set(year, month, day);
            staticReference.tieDate.setText(DateUtils.fromDateToString(pickedDate.getTime()));
        }
    }
}
