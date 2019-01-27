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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener{

    private String storeId;
    private FirebaseFirestore mFirestore;
    private ListView listViewComments;
    private static final String TAG = "TAG";
    private ProgressBar progressBar;

    private ArrayList<Comment> commentsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        mFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);

        listViewComments = findViewById(R.id.listViewComments);
        progressBar = findViewById(R.id.progressBar);
        commentsList = new ArrayList<Comment>();

        progressBar.setVisibility(View.VISIBLE);

        findViewById(R.id.buttonAddComment).setOnClickListener(this);

        Intent intent = getIntent();
        storeId = intent.getExtras().getString("storeId");

        RefreshList();
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

    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        RefreshList();
    }

    private void RefreshList(){
        mFirestore.collection("Comments")
                .whereEqualTo("storeId", storeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //String userId, String storeId, String title, String comment, Float rating
                                double ratingDouble = (Double)document.getData().get("rating");
                                float rating = (float) ratingDouble;
                                Comment newComment = new Comment(document.getData().get("userId").toString(), document.getData().get("storeId").toString(), document.getData().get("title").toString(), document.getData().get("comment").toString(), rating);
                                commentsList.add(newComment);
                            }
                            progressBar.setVisibility(View.GONE);
                            CommentsListView commentsListView = new CommentsListView(CommentsActivity.this, commentsList);
                            listViewComments.setAdapter(commentsListView);
                            listViewComments.setOnItemClickListener(new AdapterView.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
                                {
                                    Comment comment = (Comment)adapter.getItemAtPosition(position);
                                    Toast.makeText(CommentsActivity.this, comment.getTitle(), Toast.LENGTH_SHORT).show();
//                                    Intent i = new Intent(CommentsActivity.this, OfferActivity.class);
//                                    i.putExtra("commentSend", commentsList.get(position).getTitle());
//                                    startActivity(i);
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
