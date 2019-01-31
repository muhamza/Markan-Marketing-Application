package com.example.hamza.markan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener{

    private String storeId;
    private FirebaseFirestore mFirestore;
    private ListView listViewComments;
    private static final String TAG = "TAG";
    private ProgressBar progressBar;

    private ArrayList<Comment> commentsList;

    LocationManager locationManager;
    LocationListener locationListener;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
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

        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        listViewComments = findViewById(R.id.listViewComments);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        findViewById(R.id.buttonAddComment).setOnClickListener(this);

        Intent intent = getIntent();
        storeId = intent.getExtras().getString("storeId");

        RefreshList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 10, locationListener);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.buttonAddComment):
                Intent intent = new Intent(CommentsActivity.this, AddCommentActivity.class);
                Bundle extras = new Bundle();
                extras.putString("storeId", storeId);
                extras.putDouble("latitude", latitude);
                extras.putDouble("longitude", longitude);
                intent.putExtras(extras);
                startActivity(intent);
                break;
        }
    }

    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        RefreshList();
    }

    private void RefreshList(){
        commentsList = new ArrayList<Comment>();
        mFirestore.collection("Comments")
                .whereEqualTo("storeId", storeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                double ratingDouble = (Double)document.getData().get("rating");
                                float rating = (float) ratingDouble;
                                Comment newComment = new Comment(document.getData().get("userId").toString(), document.getData().get("storeId").toString(), document.getData().get("title").toString(), document.getData().get("comment").toString(), rating, (GeoPoint)document.getData().get("location"), document.getData().get("date").toString());
                                commentsList.add(newComment);
                            }
                            progressBar.setVisibility(View.GONE);
                            CommentsListView commentsListView = new CommentsListView(CommentsActivity.this, commentsList);
                            listViewComments.setAdapter(commentsListView);
                            listViewComments.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
                                {
                                    Comment comment = (Comment)adapter.getItemAtPosition(position);
                                    Toast.makeText(CommentsActivity.this, comment.getTitle(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(CommentsActivity.this, ViewCommentsActivity.class);
                                    i.putExtra("commentSend", commentsList.get(position));
                                    startActivity(i);
                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
