package com.example.sevenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SingUp extends AppCompatActivity {
    EditText signupName, signupUsername, signupEmail, signupPassword;

    Button signupButton;
    Button backButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        signupName = findViewById(R.id.singup_name);
        signupUsername = findViewById(R.id.singup_username);
        signupEmail = findViewById(R.id.singup_email);
        signupPassword = findViewById(R.id.singup_password);
        signupButton = findViewById(R.id.singup_button);
        backButton = findViewById(R.id.singup_backButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance("https://seven-41b84-default-rtdb.europe-west1.firebasedatabase.app/");
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String username = signupUsername.getText().toString();
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();

                if (!validateField(name, signupName, "Name cannot be empty") ||
                        !validateField(username, signupUsername, "Username cannot be empty") ||
                        !validateField(email, signupEmail, "Email cannot be empty") ||
                        !validateField(password, signupPassword, "Password cannot be empty")) {

                    return;
                }

                reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Користувач із вказаною електронною адресою вже існує
                            Toast.makeText(SingUp.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Користувач із вказаним ім'ям користувача вже існує
                                        Toast.makeText(SingUp.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Користувача з таким ім'ям користувача немає, можна його додати
                                        UserData userData = new UserData(name, username, email, password);
                                        reference.child(username).setValue(userData);
                                        Toast.makeText(SingUp.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SingUp.this, Login.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Обробка помилок при читанні з бази даних для імені користувача
                                    Toast.makeText(SingUp.this, "Database error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Обробка помилок при читанні з бази даних для електронної адреси
                        Toast.makeText(SingUp.this, "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingUp.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateField(String value, EditText editText, String errorMessage) {
        if (value.isEmpty()) {
            editText.setError(errorMessage);
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }
}
