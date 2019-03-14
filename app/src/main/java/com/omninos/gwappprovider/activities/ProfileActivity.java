package com.omninos.gwappprovider.activities;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.omninos.gwappprovider.BuildConfig;
import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.Utils.CommonUtils;
import com.omninos.gwappprovider.Utils.ConstantData;
import com.omninos.gwappprovider.Utils.ImageUtil;
import com.omninos.gwappprovider.ViewModels.ProviderProfileViewModel;
import com.omninos.gwappprovider.modelClasses.GetProviderProfile;
import com.omninos.gwappprovider.modelClasses.UpdateProviderProfile;

import org.w3c.dom.Text;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ProfileActivity activity;
    private ImageView backButton, editButton;
    private CircleImageView userImage;
    private TextView centerTitle;
    private Button updateProfileButton;
    private EditText userNumber, userName, userEmail, userIdNumber, expiryDate;
    private String Sname, Snumber, Semail, SidNumber, Sdate, imagepath, UserimagePath = "";
    private ProviderProfileViewModel viewModel;

    private File photoFile;
    private static final int GALLERY_REQUEST = 101;

    private static final int CAMERA_REQUEST = 102;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        activity = ProfileActivity.this;

        viewModel = ViewModelProviders.of(this).get(ProviderProfileViewModel.class);
        initView();
        SetUps();
        getProfile();
        setDisable();
    }

    private void getProfile() {
        viewModel.getProviderProfile(activity,   App.getAppPreference().GetString(activity, ConstantData.USERID)).observe(this, new Observer<GetProviderProfile>() {
            @Override
            public void onChanged(@Nullable GetProviderProfile getProviderProfile) {
                if (getProviderProfile.getSuccess().equalsIgnoreCase("1")) {

                    if (getProviderProfile.getDetails().getImage() != null) {
                        Glide.with(activity).load(getProviderProfile.getDetails().getImage()).into(userImage);
                    }
                    userNumber.setText(getProviderProfile.getDetails().getPhone());
                    userEmail.setText(getProviderProfile.getDetails().getEmail());
                    userIdNumber.setText(getProviderProfile.getDetails().getIdNumber());
                    userName.setText(getProviderProfile.getDetails().getUsername());
                    expiryDate.setText(getProviderProfile.getDetails().getExpiryDate());
                } else {
                }
            }
        });
    }

    private void setDisable() {
        userName.setEnabled(false);
        userIdNumber.setEnabled(false);
        userNumber.setEnabled(false);
        userEmail.setEnabled(false);
        expiryDate.setEnabled(false);
        userName.setEnabled(false);
        userImage.setEnabled(false);
    }

    private void initView() {
        backButton = findViewById(R.id.backButton);
        editButton = findViewById(R.id.editButton);
        centerTitle = findViewById(R.id.centerTitle);
        userNumber = findViewById(R.id.userNumber);
        userEmail = findViewById(R.id.userEmail);
        userIdNumber = findViewById(R.id.userIdNumber);
        expiryDate = findViewById(R.id.expiryDate);
        userName = findViewById(R.id.userName);
        userImage = findViewById(R.id.userImage);
        updateProfileButton = findViewById(R.id.updateProfileButton);
    }

    private void SetUps() {
        backButton.setOnClickListener(this);
        centerTitle.setText("Profile");
        editButton.setImageResource(R.drawable.ic_pencil_edit_button);
        editButton.setOnClickListener(this);
        updateProfileButton.setOnClickListener(this);
        userImage.setOnClickListener(this);
        expiryDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.editButton:
                updateProfileButton.setVisibility(View.VISIBLE);
                Enable();
                break;

            case R.id.updateProfileButton:
                updateProfile();
                break;

            case R.id.userImage:
                OpenDialog();
                break;


        }
    }

    private void OpenDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void updateProfile() {
        Sname = userName.getText().toString();
        SidNumber = userIdNumber.getText().toString();
        Snumber=userNumber.getText().toString();
        Sdate = expiryDate.getText().toString();

        if (Sname.isEmpty()) {
            Toast.makeText(activity, "enter name", Toast.LENGTH_SHORT).show();
        } else if (SidNumber.isEmpty()) {
            Toast.makeText(activity, "enter Id number", Toast.LENGTH_SHORT).show();
        } else if (Sdate.isEmpty()) {
            Toast.makeText(activity, "choose expiry date", Toast.LENGTH_SHORT).show();
        }else if (Snumber.isEmpty()){
            Toast.makeText(activity, "enter number", Toast.LENGTH_SHORT).show();
        } else {
            Update();
        }
    }

    private void Update() {


        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), Sname);

        RequestBody IdBody = RequestBody.create(MediaType.parse("text/plain"), SidNumber);

        RequestBody dateBody = RequestBody.create(MediaType.parse("text/plain"), Sdate);
        RequestBody numberBody = RequestBody.create(MediaType.parse("text/plain"), Snumber);

        RequestBody ProviderIdBody = RequestBody.create(MediaType.parse("text/plain"),   App.getAppPreference().GetString(activity, ConstantData.USERID));


        MultipartBody.Part user_image;
        if (imagepath != null) {
            File file = new File(imagepath);
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            user_image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            File file = new File(App.getAppPreference().GetString(activity, ConstantData.IMAGEPATH));
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            user_image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }
        viewModel.updateProfile(activity, ProviderIdBody, nameBody, IdBody, dateBody,numberBody,user_image).observe(this, new Observer<UpdateProviderProfile>() {
            @Override
            public void onChanged(@Nullable UpdateProviderProfile updateProviderProfile) {
                if (updateProviderProfile.getSuccess().equalsIgnoreCase("1")) {
                    if (updateProviderProfile.getDetails().getImage() != null) {
                        Glide.with(activity).load(updateProviderProfile.getDetails().getImage()).into(userImage);
                    }
                    userNumber.setText(updateProviderProfile.getDetails().getPhone());
                    userEmail.setText(updateProviderProfile.getDetails().getEmail());
                    userIdNumber.setText(updateProviderProfile.getDetails().getIdNumber());
                    userName.setText(updateProviderProfile.getDetails().getUsername());
                    expiryDate.setText(updateProviderProfile.getDetails().getExpiryDate());
                    updateProfileButton.setVisibility(View.GONE);
                    setDisable();
                } else {

                }
            }
        });
    }


    private void cameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            photoFile = null;
            photoFile = ImageUtil.getTemporaryCameraFile();
            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(ProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(pictureIntent,
                        CAMERA_REQUEST);
            }
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("profilePic/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == CAMERA_REQUEST) {
                UserimagePath = ImageUtil.compressImage(photoFile.getAbsolutePath());
                App.getAppPreference().SaveString(activity, ConstantData.IMAGEPATH, ImageUtil.compressImage(photoFile.getAbsolutePath()));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap mBitmapInsurance = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                userImage.setImageBitmap(mBitmapInsurance);
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            uri = data.getData();
            if (uri != null) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                int column_index_data = cursor.getColumnIndex(projection[0]);
                cursor.moveToFirst();
                imagepath = cursor.getString(column_index_data);
                UserimagePath = imagepath;
                App.getAppPreference().SaveString(activity, ConstantData.IMAGEPATH, UserimagePath);
                Glide.with(activity).load("file://" + imagepath).into(userImage);
            }

        }
    }

    private void Enable() {
        userName.setEnabled(true);
        userIdNumber.setEnabled(true);
        userNumber.setEnabled(true);
        userEmail.setEnabled(false);
        expiryDate.setEnabled(false);
        userName.setEnabled(true);
        userImage.setEnabled(true);
    }
}
