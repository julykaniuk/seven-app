package com.example.sevenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {
    TextView backText;
    EditText name_edit, username_edit, email_edit, password_edit;
    String nameUser, emailUser, usernameUser, passwordUser;
    TextView saveText;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        backText = findViewById(R.id.back);
        saveText = findViewById(R.id.save);
        name_edit = findViewById(R.id.name_edit);
        username_edit = findViewById(R.id.username_edit);
        email_edit = findViewById(R.id.email_edit);
        password_edit = findViewById(R.id.password_edit);

        database = FirebaseDatabase.getInstance("https://seven-41b84-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference("users");


        showData();

        backText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Викликаємо метод finish() для закриття поточної активності
                finish();
            }
        });

        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameChanged() || isEmailChanged() || isPasswordChanged() || isUserNameChanged()) {
                    Toast.makeText(EditProfile.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfile.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isNameChanged() {
        if (!nameUser.equals(name_edit.getText().toString())) {
            reference.child(usernameUser).child("name").setValue(name_edit.getText().toString());
            nameUser = name_edit.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isUserNameChanged() {
        if (!usernameUser.equals(username_edit.getText().toString())) {
            String newUsername = username_edit.getText().toString();
            reference.child(usernameUser).child("username").setValue(newUsername);
            usernameUser = newUsername;
            return true;
        } else {
            return false;
        }
    }



    public boolean isEmailChanged() {
        if (!emailUser.equals(email_edit.getText().toString())) {
            reference.child(usernameUser).child("email").setValue(email_edit.getText().toString());
            emailUser = email_edit.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public boolean isPasswordChanged() {
        if (!passwordUser.equals(password_edit.getText().toString())) {
            reference.child(usernameUser).child("password").setValue(password_edit.getText().toString());
            passwordUser = password_edit.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    public void showData() {
        Intent intent = getIntent();

        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");

        name_edit.setText(nameUser);
        email_edit.setText(emailUser);
        username_edit.setText(usernameUser);
        password_edit.setText(passwordUser);
    }
}
