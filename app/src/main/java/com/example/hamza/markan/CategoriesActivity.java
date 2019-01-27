package com.example.hamza.markan;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);
        CategoriesListView categoriesListView = new CategoriesListView(this, categories, imagesID);
        listView.setAdapter(categoriesListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                String category = (String)adapter.getItemAtPosition(position);
                Intent i = new Intent(CategoriesActivity.this, StoresActivity.class);
                i.putExtra("category", category);
                startActivity(i);
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
        }
        return true;
    }
}
