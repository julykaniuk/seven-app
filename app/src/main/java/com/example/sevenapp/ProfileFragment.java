package com.example.sevenapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    TextView edit;
    private DatabaseReference mDatabase;
    private TextView userNameView;
    private String userName;
    Button notificationButton, changeLanguage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        edit = view.findViewById(R.id.edit);
        userNameView = view.findViewById(R.id.textView);
        notificationButton=view.findViewById(R.id.changeNotificatinn);
        changeLanguage=view.findViewById(R.id.changeLanguage);
        Bundle arguments = getArguments();
        String userKey = arguments != null ? arguments.getString("USER_KEY") : null;

        if (userKey != null) {
            mDatabase = FirebaseDatabase.getInstance("https://seven-41b84-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users").child(userKey);

            // Отримати ім'я користувача з Firebase Realtime Database
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String userName = dataSnapshot.child("username").getValue(String.class);
                    if (userName != null) {
                        userNameView.setText(userName); // Відображення імені користувача
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Обробка помилок, якщо потрібно
                }
            });
    }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                intent.putExtra("username", userName);
                startActivity(intent);
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),SettingsActivity.class);
                startActivity(intent);
            }
        });
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),LanguageChange.class);
                startActivity(intent);
            }
        });
        return view;
    }
}