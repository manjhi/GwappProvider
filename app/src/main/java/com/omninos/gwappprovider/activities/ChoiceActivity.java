package com.omninos.gwappprovider.activities;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.omninos.gwappprovider.BuildConfig;
import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.Utils.ImageUtil;

import java.io.File;

public class ChoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner, spinner1;
    private Button nextButton;
    private ChoiceActivity activity;
    private ImageView insuranceImage, licenceImage;
    private int change = 0;
    private String S_type = "-Select-", S_Qualification = "-Select-";
    private static final String[] Types = {"-Select-", "Professional", "General Services"};
    private static final String[] Qualifications = {"-Select-", "Construction", "Maintenance", "Food"};
    private File photoFile;
    private static final int GALLERY_REQUEST = 101;

    private static final int CAMERA_REQUEST = 102;
    private EditText perHourChange;

    private Uri uri;
    private String NINumber, imagepath = "", LicenceImagePath = "", InsuranceImagePath = "",Charges="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        activity = ChoiceActivity.this;
        initView();
        SetUps();

    }

    //initiate all views
    private void initView() {
        spinner = findViewById(R.id.spinner);
        spinner1 = findViewById(R.id.spinner1);
        nextButton = findViewById(R.id.nextButton);
        licenceImage = findViewById(R.id.licenceImage);
        insuranceImage = findViewById(R.id.insuranceImage);
        perHourChange = findViewById(R.id.perHourChange);
    }

    //set Actions
    private void SetUps() {

        licenceImage.setOnClickListener(this);
        insuranceImage.setOnClickListener(this);

        //Set type spinner Adpater
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChoiceActivity.this,
                android.R.layout.simple_spinner_item, Types);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                S_type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //set Qualification spinner Adapter
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ChoiceActivity.this,
                android.R.layout.simple_spinner_item, Qualifications);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                S_Qualification = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //click listener
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                Validate();
//
                break;

            case R.id.licenceImage:
                change = 1;
                selectImage();
                break;

            case R.id.insuranceImage:
                change = 2;
                selectImage();
                break;
        }
    }


    //choose image dialogBox
    private void selectImage() {
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

    //choose from Gallery
    private void galleryIntent() {


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("profilePic/*");
        startActivityForResult(intent, GALLERY_REQUEST);

    }


    //Choose from Camera
    private void cameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            photoFile = null;
            photoFile = ImageUtil.getTemporaryCameraFile();
            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(ChoiceActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(pictureIntent,
                        CAMERA_REQUEST);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == CAMERA_REQUEST) {
                if (change == 1) {
                    //set Licence imagePath
                    LicenceImagePath = ImageUtil.compressImage(photoFile.getAbsolutePath());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    Bitmap mBitmapInsurance = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                    //set Image on ImageView
                    licenceImage.setImageBitmap(mBitmapInsurance);
                    photoFile.delete();
                } else if (change == 2) {
                    //set Insurance Image
                    InsuranceImagePath = ImageUtil.compressImage(photoFile.getAbsolutePath());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    Bitmap mBitmapInsurance = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                    // set Image on ImageView
                    insuranceImage.setImageBitmap(mBitmapInsurance);
                    photoFile.delete();
                }
            }
        }
    }

    //validations
    private void Validate() {
         Charges=perHourChange.getText().toString();

        if (S_type.equalsIgnoreCase("-Select-")) {
            Toast.makeText(activity, "Select type", Toast.LENGTH_SHORT).show();
        } else if (S_Qualification.equalsIgnoreCase("-Select-")) {
            Toast.makeText(activity, "Select qualification", Toast.LENGTH_SHORT).show();
        } else if (LicenceImagePath.isEmpty()) {
            Toast.makeText(activity, "Choose Licence Image", Toast.LENGTH_SHORT).show();
        } else if (InsuranceImagePath.isEmpty()) {
            Toast.makeText(activity, "Choose Insurance Image", Toast.LENGTH_SHORT).show();
        } else if (Charges.isEmpty()) {
            perHourChange.setError("Enter charges");
        } else {
            //save temporary Data
            App.getSinltonPojo().setProviderServiceType(S_type);
            App.getSinltonPojo().setProviderQualification(S_Qualification);
            App.getSinltonPojo().setProviderInsurance(InsuranceImagePath);
            App.getSinltonPojo().setDrivingLicence(LicenceImagePath);
            App.getSinltonPojo().setCharges(Charges);
            startActivity(new Intent(activity, ServicesListActivity.class));
        }
    }


    //select from gallery
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            uri = data.getData();
            if (uri != null) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                int column_index_data = cursor.getColumnIndex(projection[0]);
                cursor.moveToFirst();
                imagepath = cursor.getString(column_index_data);

                if (change == 1) {
                    //set Image on Imageview
                    LicenceImagePath = imagepath;
                    Glide.with(activity).load("file://" + imagepath).into(licenceImage);
                } else if (change == 2) {
                    //set Image on Imageview
                    InsuranceImagePath = imagepath;
                    Glide.with(activity).load("file://" + imagepath).into(insuranceImage);
                }
            }

        }
    }
}
