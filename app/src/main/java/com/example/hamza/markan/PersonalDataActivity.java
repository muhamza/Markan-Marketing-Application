package com.example.hamza.markan;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class PersonalDataActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextFirstName, editTextLastName;
    RadioGroup radioGenderGroup;
    RadioButton radioGenderButton, radioButton;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        mAuth = FirebaseAuth.getInstance();

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        radioGenderGroup = findViewById(R.id.radioGender);
        radioButton = findViewById(R.id.radioMale);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.buttonSaveInformation).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSaveInformation:
                SaveInformation();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    //Hide keyboard when the user clicks the background
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    private void SaveInformation() {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();

        if (firstName.isEmpty()){
            editTextFirstName.setError("Email is required!");
            editTextFirstName.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            editTextLastName.setError("Email is required!");
            editTextLastName.requestFocus();
            return;
        }
        if (radioGenderGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(PersonalDataActivity.this, "Please choose a gender!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGenderGroup.getCheckedRadioButtonId();
        radioGenderButton = findViewById(selectedId);

        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            String name = firstName + " " + lastName;
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(PersonalDataActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(PersonalDataActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
