package com.example.hamza.markan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewCommentsActivity extends AppCompatActivity {
    private TextView textViewDate;
    private EditText editTextTitle, editTextComment;
    private RatingBar ratingBarComment;
    Comment comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);

        textViewDate = findViewById(R.id.textViewDate);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextComment = findViewById(R.id.editTextComment);
        ratingBarComment = findViewById(R.id.ratingBarComment);

        Intent intent = getIntent();
        comment = intent.getParcelableExtra("commentSend");

        textViewDate.setText(comment.getDate());
        editTextTitle.setText(comment.getTitle());
        editTextComment.setText(comment.getComment());
        ratingBarComment.setRating((float)comment.getRating());

    }
}
