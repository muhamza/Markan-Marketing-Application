package com.example.hamza.markan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener{

    private String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        findViewById(R.id.buttonAddComment).setOnClickListener(this);

        Intent intent = getIntent();
        storeId = intent.getExtras().getString("storeId");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.buttonAddComment):
                Intent i = new Intent(CommentsActivity.this, AddCommentActivity.class);
                i.putExtra("storeId", storeId);
                startActivity(i);
                break;
        }
    }
}
