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
import com.omninos.gwappprovider.ViewModels.RegisterLoginViewModel;
import com.omninos.gwappprovider.modelClasses.CheckEmailPhoneModel;

import org.w3c.dom.Text;

import java.io.File;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tv_signin;
    private Button signUp;
    private EditText userName, userEmail, userPassword, ConfirmPass, Number;
    private String S_username, S_userEmail, S_userPass, S_userConfirmPass, S_Number, imagepath, UserimagePath = "";
    private RegisterLoginViewModel viewModel;
    private ImageView userImage;
    private RegisterActivity activity;
    private TextView tv_confirm, tv_pass;

    private File photoFile;
    private static final int GALLERY_REQUEST = 101;

    private static final int CAMERA_REQUEST = 102;

    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = RegisterActivity.this;
        viewModel = ViewModelProviders.of(this).get(RegisterLoginViewModel.class);

        initView();
        SetUps();
    }

    //initiate all Id's
    private void initView() {
        tv_signin = findViewById(R.id.tv_signin);
        signUp = findViewById(R.id.signUp);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        ConfirmPass = findViewById(R.id.ConfirmPass);
        Number = findViewById(R.id.Number);
        userImage = findViewById(R.id.userImage);
        tv_pass = findViewById(R.id.tv_pass);
        tv_confirm = findViewById(R.id.tv_confirm);
    }


    //setup Actions
    private void SetUps() {
        tv_signin.setOnClickListener(this);
        signUp.setOnClickListener(this);
        userImage.setOnClickListener(this);


//
//        Number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//
//                } else {
//                    String phone = Number.getText().toString();
//                    CheckPhoneNumber(phone);
//                }
//            }
//        });

        if (App.getSinltonPojo().getSocialLoginStatus() != null) {
            if (App.getSinltonPojo().getSocialLoginStatus().equalsIgnoreCase("SocialLogin")) {
//                App.getSinltonPojo().setSocialLoginStatus(null);
                userPassword.setVisibility(View.GONE);
                ConfirmPass.setVisibility(View.GONE);
                tv_pass.setVisibility(View.GONE);
                tv_confirm.setVisibility(View.GONE);
                userImage.setEnabled(false);
                userName.setText(getIntent().getStringExtra("name"));
                userEmail.setText(getIntent().getStringExtra("email"));
                imagepath = getIntent().getStringExtra("image");
                Glide.with(activity).load(imagepath).into(userImage);

            } else {
                userPassword.setVisibility(View.VISIBLE);
                ConfirmPass.setVisibility(View.VISIBLE);
                tv_pass.setVisibility(View.VISIBLE);
                tv_confirm.setVisibility(View.VISIBLE);

                //check email exit or not
                userEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {

                        } else {
                            String email = userEmail.getText().toString();
                            CheckEmail(email);
                        }
                    }
                });

            }
        }
    }

    //check phone number exit or not through API
    private void CheckPhoneNumber(final String phone) {
        viewModel.phone(RegisterActivity.this, phone).observe(this, new Observer<CheckEmailPhoneModel>() {
            @Override
            public void onChanged(@Nullable CheckEmailPhoneModel checkEmailPhoneModel) {
                if (checkEmailPhoneModel.getSuccess().equalsIgnoreCase("1")) {
                    App.getSinltonPojo().setProviderPhone(phone);
                    startActivity(new Intent(RegisterActivity.this, ChoiceActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, checkEmailPhoneModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //check email through API
    private void CheckEmail(String email) {
        viewModel.email(RegisterActivity.this, email).observe(this, new Observer<CheckEmailPhoneModel>() {
            @Override
            public void onChanged(@Nullable CheckEmailPhoneModel checkEmailPhoneModel) {
                if (checkEmailPhoneModel.getSuccess().equalsIgnoreCase("1")) {

                } else {
                    Toast.makeText(RegisterActivity.this, checkEmailPhoneModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_signin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;

            case R.id.signUp:
                validate();
                break;

            case R.id.userImage:
                OpenImageData();
                break;
        }
    }

    //choose userImage
    private void OpenImageData() {
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


    //choose from camera
    private void cameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            photoFile = null;
            photoFile = ImageUtil.getTemporaryCameraFile();
            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(RegisterActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(pictureIntent,
                        CAMERA_REQUEST);
            }
        }
    }

    //Choose from gallery
    private void galleryIntent() {
        //gallery intent
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
                //set image path
                UserimagePath = ImageUtil.compressImage(photoFile.getAbsolutePath());
                App.getAppPreference().SaveString(activity, ConstantData.IMAGEPATH, ImageUtil.compressImage(photoFile.getAbsolutePath()));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap mBitmapInsurance = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                //set image preview on imageView
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
                //set image preview on Image View
                Glide.with(activity).load("file://" + imagepath).into(userImage);
            }

        }
    }

    //validations
    private void validate() {
        if (App.getSinltonPojo().getSocialLoginStatus() != null) {
            S_username = userName.getText().toString().trim();
            S_userEmail = userEmail.getText().toString().trim();
            S_Number = Number.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2,4}";
//        String namePattern = "[a-zA-z ]";
//            if (getIntent().getStringExtra("image").isEmpty()) {
//                Toast.makeText(activity, "Choose Image", Toast.LENGTH_SHORT).show();
//                userImage.setEnabled(true);
            if (S_username.isEmpty()) {
                userName.setError("enter username");
            } else if (S_userEmail.isEmpty() || !S_userEmail.matches(emailPattern)) {
                userEmail.setError("enter valid email pattern");
            } else if (S_Number.isEmpty() || S_Number.length() < 9) {
                Number.setError("enter valid number length");
            } else {
                //save temporary data
                App.getSinltonPojo().setUserimagePath(getIntent().getStringExtra("image"));
                App.getSinltonPojo().setProviderName(S_username);
                App.getSinltonPojo().setProviderEmail(S_userEmail);
                CheckPhoneNumber(S_Number);
            }
        } else {
            S_username = userName.getText().toString().trim();
            S_userEmail = userEmail.getText().toString().trim();
            S_userPass = userPassword.getText().toString().trim();
            S_userConfirmPass = ConfirmPass.getText().toString().trim();
            S_Number = Number.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]{2,4}";
//        String namePattern = "[a-zA-z ]";
            if (UserimagePath.isEmpty()) {
                Toast.makeText(activity, "Choose Image", Toast.LENGTH_SHORT).show();
            } else if (S_username.isEmpty() || !S_username.matches("^[A-Za-z\\s]*$")) {
                userName.setError("enter username");
            } else if (S_userEmail.isEmpty() || !S_userEmail.matches(emailPattern)) {
                userEmail.setError("enter valid email pattern");
            } else if (S_userPass.isEmpty() || S_userPass.length() < 7) {
                userPassword.setError("enter minimum 7 character password");
            } else if (S_userConfirmPass.isEmpty() || !S_userConfirmPass.equals(S_userPass)) {
                ConfirmPass.setError("password mismatch");
            } else if (S_Number.isEmpty() || S_Number.length() < 9) {
                Number.setError("enter valid number length");
            } else {
                //save temporary data
                App.getSinltonPojo().setUserimagePath(UserimagePath);
                App.getSinltonPojo().setProviderName(S_username);
                App.getSinltonPojo().setProviderEmail(S_userEmail);
                App.getSinltonPojo().setProviderPassword(S_userPass);
                CheckPhoneNumber(S_Number);
            }
        }


    }
}
