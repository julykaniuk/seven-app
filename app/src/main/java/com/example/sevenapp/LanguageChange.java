package com.example.sevenapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import android.os.Bundle;

public class LanguageChange extends AppCompatActivity {
    private RadioGroup languageRadioGroup;
    private RadioButton englishRadio;
    private RadioButton ukrainianRadio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_change);

        languageRadioGroup = findViewById(R.id.language_radio_group);
        englishRadio = findViewById(R.id.english_radio);
        ukrainianRadio = findViewById(R.id.ukrainian_radio);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            int selectedId = languageRadioGroup.getCheckedRadioButtonId();

            if (selectedId == englishRadio.getId()) {
                setLocale("en");
            } else if (selectedId == ukrainianRadio.getId()) {
                setLocale("uk");
            }

            Intent backIntent = new Intent(this, MainActivity.class);
            startActivity(backIntent);
            finish();
        });

        setInitialCheckedRadio();
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish(); // Завершуємо поточну активність
        });
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void setInitialCheckedRadio() {
        String currentLang = Locale.getDefault().getLanguage();
        if (currentLang.equals("en")) {
            englishRadio.setChecked(true);
        } else if (currentLang.equals("uk")) {
            ukrainianRadio.setChecked(true);
        }
    }
}