package com.example.hamza.markan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ViewCommentsActivity extends AppCompatActivity {
    private TextView textViewDate;
    private EditText editTextTitle, editTextComment;
    private RatingBar ratingBarComment;
    private String storeName;
    Comment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);

        setTitle("Comment View");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
}
