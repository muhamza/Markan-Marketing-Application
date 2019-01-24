package com.example.hamza.markan;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class StoreActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private ListView listViewStores;
    private static final String TAG = "TAG";
    private ProgressBar progressBar;

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
        String category = intent.getExtras().getString("category");

        mFirestore.collection("Stores")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Store store = new Store(document.getId(), document.getData().get("name").toString(), document.getData().get("category").toString(), document.getData().get("details").toString(), (GeoPoint) document.getData().get("coordinates"), document.getData().get("image").toString(), document.getData().get("logo").toString(), document.getData().get("tagline").toString());
                                storesList.add(store);
                            }
                            progressBar.setVisibility(View.GONE);
                            StoreListView storeListView = new StoreListView(StoreActivity.this, storesList);
                            listViewStores.setAdapter(storeListView);
                            listViewStores.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
                                {
                                    Store store = (Store)adapter.getItemAtPosition(position);
//                                    Toast.makeText(StoreActivity.this, store.getStoreName(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(StoreActivity.this, OfferActivity.class);
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

}
