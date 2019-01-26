package com.example.hamza.markan;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class AddCommentActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseFirestore mFirestore;
    private FirebaseUser currentFirebaseUser;
    private EditText editTextTitle, editTextComment;
    private RatingBar ratingBarComment;
    private ProgressBar progressBar;
    private String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextComment = findViewById(R.id.editTextComment);
        ratingBarComment = findViewById(R.id.ratingBarComment);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.buttonAddComment).setOnClickListener(this);

        Intent intent = getIntent();
        storeId = intent.getExtras().getString("storeId");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case(R.id.buttonAddComment):
                AddComment();
        }
    }
    //Hide keyboard when the user clicks the background
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    private void AddComment(){
        String title = editTextTitle.getText().toString();
        String comment = editTextComment.getText().toString();

        if (title.isEmpty()){
            editTextTitle.setError("Title is required!");
            editTextTitle.requestFocus();
            return;
        }
        if (comment.isEmpty()){
            editTextComment.setError("A Comment is required!");
            editTextComment.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        final String userId = currentFirebaseUser.getUid();

        //Add Comment to database
        Comment newComment = new Comment(userId, storeId, editTextTitle.getText().toString(), editTextComment.getText().toString(), ratingBarComment.getRating());
        mFirestore.collection("Comments").add(newComment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(AddCommentActivity.this, "Successfully, added the comment.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddCommentActivity.this, CommentsActivity.class));
                }
                else{
                    Toast.makeText(AddCommentActivity.this, "The comment could not be added. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
