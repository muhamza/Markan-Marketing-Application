package com.example.hamza.markan;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class StoresActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private ListView listViewStores;
    private static final String TAG = "TAG";
    private ProgressBar progressBar;
    private String category;
    private float radius;
    private Location userLocation;
    private Location storeLocation;
    private GeoPoint storeLocationGeo;
    private float distance;

    private ArrayList<Store> storesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        listViewStores = findViewById(R.id.listViewStores);
        progressBar = findViewById(R.id.progressBar);
        storesList = new ArrayList<Store>();

        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        category = extras.getString("category");
        double latitude = extras.getDouble("latitude");
        double longitude = extras.getDouble("longitude");
        radius = (float)extras.getDouble("radius");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(category);

        userLocation = new Location("");
        userLocation.setLatitude(latitude);
        userLocation.setLongitude(longitude);
        Log.i("userLocation", userLocation.toString());

        mFirestore.collection("Stores")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                storeLocationGeo = (GeoPoint) document.getData().get("coordinates");
                                storeLocation = new Location("");
                                storeLocation.setLatitude(storeLocationGeo.getLatitude());
                                storeLocation.setLongitude(storeLocationGeo.getLongitude());
//                                Log.i("storeLocation", storeLocation.toString());

                                distance = userLocation.distanceTo(storeLocation)/1000;
//                                Log.i("distance", String.valueOf(distance));
                                if (distance < radius){
                                    Store store = new Store(document.getId(), document.getData().get("name").toString(), document.getData().get("category").toString(), document.getData().get("details").toString(), storeLocationGeo, document.getData().get("image").toString(), document.getData().get("logo").toString(), document.getData().get("tagline").toString());
                                    storesList.add(store);
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            StoresListView storesListView = new StoresListView(StoresActivity.this, storesList);
                            listViewStores.setAdapter(storesListView);
                            listViewStores.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
                                {
                                    Store store = (Store)adapter.getItemAtPosition(position);
//                                    Toast.makeText(StoresActivity.this, store.getStoreName(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(StoresActivity.this, OfferActivity.class);
                                    i.putExtra("storeSend", storesList.get(position));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        Bundle extras;

        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menuOneKm:
                RefreshActivity(1);
                break;
            case R.id.menuFiveKm:
                RefreshActivity(5);
                break;
            case R.id.menuTenKm:
                RefreshActivity(10);
                break;
            case R.id.menuRefresh:
                RefreshActivity(5);
                break;
            case R.id.menuHome:
                intent = new Intent(this, CategoriesActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void RefreshActivity(float radius){
        Intent intent = new Intent(this, StoresActivity.class);
        Bundle extras = new Bundle();
        extras.putString("category", category);
        extras.putDouble("latitude", userLocation.getLatitude());
        extras.putDouble("longitude", userLocation.getLongitude());
        extras.putDouble("radius", radius);
        intent.putExtras(extras);
        startActivity(intent);
    }

}
