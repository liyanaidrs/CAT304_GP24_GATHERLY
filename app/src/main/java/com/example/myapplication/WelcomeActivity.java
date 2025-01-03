package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Retrieve the username from the intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        // Delay for 3 seconds before transitioning to HomeActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent homeIntent = new Intent(WelcomeActivity.this, HomeActivity.class);
            homeIntent.putExtra("username", username);
            startActivity(homeIntent);
            finish(); // Finish WelcomeActivity so user can't return to it
        }, 3000);

        // If "Get Started" button is clicked, transition immediately
        findViewById(R.id.get_started_button).setOnClickListener(v -> {
            Intent homeIntent = new Intent(WelcomeActivity.this, HomeActivity.class);
            homeIntent.putExtra("username", username);
            startActivity(homeIntent);
            finish();
        });
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}