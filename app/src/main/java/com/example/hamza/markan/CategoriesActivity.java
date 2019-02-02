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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;


public class CategoriesActivity extends AppCompatActivity{
    ListView listView;
    String[] categories = {"Clothing", "Food", "Footwear", "Grocery", "Handbags"};
    Integer[] imagesID = {R.drawable.clothing, R.drawable.food, R.drawable.footwear, R.drawable.grocery, R.drawable.handbag};
    LocationManager locationManager;
    LocationListener locationListener;
//    Double latitude, longitude;
    Location userLocation;
    AlertDialog.Builder error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Categories");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = location;
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

        listView = (ListView) findViewById(R.id.listView);
        CategoriesListView categoriesListView = new CategoriesListView(this, categories, imagesID);
        listView.setAdapter(categoriesListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                if (userLocation != null){
                    String category = (String)adapter.getItemAtPosition(position);
                    Intent intent = new Intent(CategoriesActivity.this, StoresActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("category", category);
                    extras.putDouble("latitude", userLocation.getLatitude());
                    extras.putDouble("longitude", userLocation.getLongitude());
                    extras.putDouble("radius", 10);
                    intent.putExtras(extras);
                    //intent.putExtra("category", category);
                    startActivity(intent);
                }
                else{
                    AlertDialog alert = error.create();
                    alert.show();
                }
            }
        });
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
                finish();
                startActivity(getIntent());
                break;
        }
        return true;
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
}
