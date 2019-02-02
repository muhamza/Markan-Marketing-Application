package com.example.hamza.markan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfferActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewStoreName, textViewTagline, textViewDetails;
    ImageView imageViewLogo, imageViewImage;
    Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        textViewStoreName = findViewById(R.id.textViewStoreName);
        textViewTagline = findViewById(R.id.textViewTagline);
        textViewDetails = findViewById(R.id.textViewDetails);
        imageViewImage = findViewById(R.id.imageViewImage);
        imageViewLogo = findViewById(R.id.imageViewLogo);

        findViewById(R.id.buttonComments).setOnClickListener(this);

        Intent intent = getIntent();
        store = intent.getParcelableExtra("storeSend");

        textViewStoreName.setText(store.getStoreName());
        textViewTagline.setText(store.getTagline());
        textViewDetails.setText(store.getDetails());

        Picasso.get().load(store.getLogo()).fit().into(imageViewLogo);
        Picasso.get().load(store.getImage()).fit().into(imageViewImage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.buttonComments):
                Intent intent = new Intent(OfferActivity.this, CommentsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("storeId", store.getId());
                extras.putString("storeName", store.getStoreName());
                intent.putExtras(extras);
                startActivity(intent);
                break;
        }
    }
}
