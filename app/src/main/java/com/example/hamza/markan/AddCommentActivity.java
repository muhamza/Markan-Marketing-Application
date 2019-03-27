package com.example.hamza.markan;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddCommentActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseFirestore mFirestore;
    private FirebaseUser currentFirebaseUser;
    private EditText editTextTitle, editTextComment;
    private RatingBar ratingBarComment;
    private ProgressBar progressBar;
    private String storeId, storeName;
    private double latitude, longitude;
    GeoPoint userLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    AlertDialog.Builder error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        error = new AlertDialog.Builder(this);
        error.setMessage("Please turn on location services.");
        error.setCancelable(true);
        error.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            //we have permissions
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 10, locationListener);
        }

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextComment = findViewById(R.id.editTextComment);
        ratingBarComment = findViewById(R.id.ratingBarComment);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.buttonAddComment).setOnClickListener(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        storeId = extras.getString("storeId");
        storeName = extras.getString("storeName");

        setTitle(storeName + " Add Comment");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 10, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                userLocation = new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.buttonAddComment):
                AddComment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menuHome:
                Intent intent = new Intent(this, CategoriesActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    //Hide keyboard when the user clicks the background
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    private void AddComment(){
        String title = editTextTitle.getText().toString();
        String comment = editTextComment.getText().toString();

        if (title.isEmpty()){
            editTextTitle.setError("Title is required!");
            editTextTitle.requestFocus();
            return;
        }
        if (comment.isEmpty()){
            editTextComment.setError("A Comment is required!");
            editTextComment.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        final String userId = currentFirebaseUser.getUid();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        //Add Comment to database
        if (userLocation != null){
            Comment newComment = new Comment(userId, storeId, title, comment, ratingBarComment.getRating(), userLocation, currentDate);
            mFirestore.collection("Comments").add(newComment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()){
                        Toast.makeText(AddCommentActivity.this, "Successfully, added the comment.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(AddCommentActivity.this, "The comment could not be added. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Log.i("userLocation", userLocation.toString());
        }
        else{
            AlertDialog alert = error.create();
            alert.show();
        }
    }
}
