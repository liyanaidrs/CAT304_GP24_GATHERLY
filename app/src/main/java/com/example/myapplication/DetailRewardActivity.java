package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailRewardActivity extends AppCompatActivity {

    private TextView tvEventTitle, tvEventDate, tvEventDesc, tvEventLang, tvReward, tvMessage;
    private ImageView ivEventImage, closeIcon;
    private RatingBar rbRatingBar;
    private DatabaseReference eventCreatedRef;
    private float eventStars; // Variable to store the number of stars

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reward);

        // Initialize UI components
        tvEventTitle = findViewById(R.id.tvEventTitle);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventDesc = findViewById(R.id.tvEventDesc);
        tvEventLang = findViewById(R.id.tvEventLang);
        ivEventImage = findViewById(R.id.ivEventImage);
        tvReward = findViewById(R.id.tvReward);
        rbRatingBar = findViewById(R.id.rbRatingBar);
        tvMessage = findViewById(R.id.tvMessage);

        // Set the RatingBar to 5 stars by default
        rbRatingBar.setRating(5.0f); // Full 5 stars

        // Get eventKey from Intent
        String eventKey = getIntent().getStringExtra("eventKey");
        if (eventKey == null || eventKey.isEmpty()) {
            Toast.makeText(this, "Event Key not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase reference
        eventCreatedRef = FirebaseDatabase.getInstance().getReference("Event Created").child(eventKey);

        // Close the activity on close icon click
        ImageView closeIcon = findViewById(R.id.cancelButton);
        closeIcon.setOnClickListener(v -> finish());

        // Load event details
        loadEventDetails();
    }

    private void loadEventDetails() {
        eventCreatedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String title = snapshot.child("dataTitle").getValue(String.class);
                    String date = snapshot.child("dataDate").getValue(String.class);
                    String desc = snapshot.child("dataDesc").getValue(String.class);
                    String lang = snapshot.child("dataLang").getValue(String.class);
                    String imageUrl = snapshot.child("dataImage").getValue(String.class);
                    Float stars = snapshot.child("dataStars").getValue(Float.class); // Assuming 'dataStars' is the field in Firebase for star rating

                    // Populate UI components
                    tvEventTitle.setText(title != null ? title : "N/A");
                    tvEventDate.setText(date != null ? date : "N/A");
                    tvEventDesc.setText(desc != null ? desc : "N/A");
                    tvEventLang.setText(lang != null ? lang : "N/A");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(DetailRewardActivity.this).load(imageUrl).into(ivEventImage);
                    }

                    // Optional: Set the rating based on Firebase data
                    if (stars != null) {
                        rbRatingBar.setRating(stars); // Set the rating based on stars from Firebase
                    }
                } else {
                    Toast.makeText(DetailRewardActivity.this, "Event details not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailRewardActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
