package com.example.hamza.markan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfferActivity extends AppCompatActivity {

    TextView textViewStoreName, textViewTagline, textViewDetails;
    ImageView imageViewLogo, imageViewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        textViewStoreName = findViewById(R.id.textViewStoreName);
        textViewTagline = findViewById(R.id.textViewTagline);
        textViewDetails = findViewById(R.id.textViewDetails);
        imageViewImage = findViewById(R.id.imageViewImage);
        imageViewLogo = findViewById(R.id.imageViewLogo);

        Intent intent = getIntent();
        Store store = intent.getParcelableExtra("storeSend");

        textViewStoreName.setText(store.getStoreName());
        textViewTagline.setText(store.getTagline());
        textViewDetails.setText(store.getDetails());

        Picasso.get().load(store.getLogo()).fit().into(imageViewLogo);
        Picasso.get().load(store.getImage()).fit().into(imageViewImage);

//        Log.d("id", store.getId());
//        Log.d("name", store.getStoreName());
//        Log.d("details", store.getDetails());
//        Log.d("tagline", store.getTagline());
//        Log.d("image", store.getImage());
//        Log.d("logo", store.getLogo());
//        Log.d("category", store.getCategory());
//        Log.d("coordinates", store.getCoordinates().toString());
    }
}
