package com.example.supplementseshopmanagmentsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{

    private EditText userUsername;
    private EditText userPassword;
    private Button loginButton;
    private CheckBox loginCheckBox;
    private sharedPreferenceConfig sharedPreference;

    private boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userUsername = findViewById(R.id.loginUsername);
        userPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        loginCheckBox = findViewById(R.id.loginCheckBox);
        sharedPreference = new sharedPreferenceConfig(getApplicationContext());

        if (sharedPreference.readLoginStatus()) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        loginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked = true;
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(validateUsername() && validatePassword()){
                    checkUser();
                }
            }
        });

    }

    public boolean validateUsername(){

        String editUserTxt = userUsername.getText().toString();

        if(editUserTxt.isEmpty()){
            userUsername.setError("Username can not be empty");
            return false;
        }else{
            userUsername.setError(null);
            return true;
        }
    }

    public boolean validatePassword(){

        String editPasswTxt = userPassword.getText().toString();

        if(editPasswTxt.isEmpty()){
            userPassword.setError("Password can not be empty");
            return false;
        }else{
            userPassword.setError(null);
            return true;
        }
    }

    public void checkUser(){

        String loginUsername = userUsername.getText().toString().trim();
        String loginPassword = userPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://pchardwareeshopmanagmentapp-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(loginUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    userUsername.setError(null);
                    String dbPassword = snapshot.child(loginUsername).child("password").getValue(String.class);

                    if(dbPassword.equals(loginPassword)){
                        userPassword.setError(null);
                        if(checked){
                            sharedPreference.writeLoginStatus(true);
                            sharedPreference.writeUsername(loginUsername);
                        }

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        userPassword.setError("Invalid password");
                        userPassword.requestFocus();
                    }
                }else{
                    userUsername.setError("The username does not exist");
                    userUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}