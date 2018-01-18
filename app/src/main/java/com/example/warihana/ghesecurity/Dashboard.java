package com.example.warihana.ghesecurity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Dashboard extends AppCompatActivity {
    ImageButton callCameraButton;
    ImageView photoImage;
    Button btnBrowse;
    Button logout;
    EditText description;
    Spinner mySpinner;
    Button report;
    Bitmap bitmap;

    private static final String TAG = "CallCamera";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 10;
    Uri fileUri = null;
    public static final int RESULT_LOAD_IMG = 11;
    public static final int LOCATION_CODE = 12;
    LocationManager locationManager;
    String lat = "";
    String lng = "";

    static final String appDirectoryName = "XYZ";
    static final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES), appDirectoryName);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        photoImage = findViewById(R.id.photo_image);
        photoImage.setImageDrawable(null);
        //when logout button is clicked
        logout = findViewById(R.id.btnlogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Intent dashboard = new Intent(Dashboard.this, Login.class);
                startActivity(dashboard);
            }
        });

        callCameraButton = findViewById(R.id.btncamera);
        callCameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (fileUri != null) {
                    fileUri = Uri.fromFile(getOutputPhotoFile());

                }
                startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ);

            }
        });

        btnBrowse = findViewById(R.id.btnBrowse);
        btnBrowse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        mySpinner = findViewById(R.id.spCrimeType);
        description = findViewById(R.id.txtdescription);
        report = findViewById(R.id.btnreport);


        report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                getMyLocation();
                String crimeCategory = mySpinner.getSelectedItem().toString();
                String crimeDescription = description.getText().toString();
                String type = "insert";
                String image = getStringImage(bitmap);
                String location = lat + "," + lng;
                //Toast.makeText(getApplicationContext(), location, Toast.LENGTH_SHORT).show();
                BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                backgroundTask.execute(type, crimeDescription, crimeCategory, image, location);

            }
        });

    }

    private File getOutputPhotoFile() {

        if (!imageRoot.exists()) {
            if (!imageRoot.mkdirs()) {
                Log.e(TAG, "Failed to create storage directory.");
                return null;
            }
        }
        return new File(imageRoot, "image1.jpg");
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");

                    Toast.makeText(this, "Image captured successfully",
                            Toast.LENGTH_LONG).show();
                    //photoUri = fileUri;
                    photoImage.setImageBitmap(bitmap);
                } else {

                    Toast.makeText(this, "Image couldnt saved successfully in: " + data.getData(),
                            Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Callout for image capture failed!",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == RESULT_LOAD_IMG && data != null) {

            if (resultCode == RESULT_OK) {
                try {

                    Toast.makeText(this, "Callout for image success!",
                            Toast.LENGTH_LONG).show();
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                    photoImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void showPhoto(Uri photoUri) {
        File imageFile = new File(String.valueOf(photoUri));
        if (imageFile.exists()) {
            Drawable oldDrawable = photoImage.getDrawable();
            if (oldDrawable != null) {
                ((BitmapDrawable) oldDrawable).getBitmap().recycle();
            }

            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
            photoImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
            photoImage.setImageDrawable(drawable);
        }
    }

    //convert image to string
    public String getStringImage(Bitmap bitmap) {
        Log.i("ghsecurity", "" + bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String image = Base64.encodeToString(b, Base64.DEFAULT);
        return image;
    }

    //get uer location
    public void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_CODE);
        } else {
            Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (loc != null) {

                lat = String.valueOf(loc.getLatitude());
                lng = String.valueOf(loc.getLongitude());
                Toast.makeText(this, "getting location...." + lat + " " + lng, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "could not get location", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_CODE:
                getMyLocation();
                break;

            default:
                Log.d("testing", "GPS");
                break;
        }

    }
}
