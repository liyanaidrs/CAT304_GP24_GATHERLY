package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailListActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang, detailDate, c1, c2, c3, status;
    ImageView detailImage;
    String key = "";
    String imageUrl = "";
    String username;
    Button chat;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list);

        // Initialize views
        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailLang = findViewById(R.id.detailLang);
        detailDate = findViewById(R.id.detailDate);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        status = findViewById(R.id.status);
        chat=findViewById(R.id.chatbutton);
        ImageView closeIcon = findViewById(R.id.cancelButton);

        // Firebase initialization
        databaseReference = FirebaseDatabase.getInstance().getReference("EventCheckpoints");

        // Retrieve event details from intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            detailDate.setText(bundle.getString("Date"));
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
            username = bundle.getString("currentUser");
        }

        // Handle missing user case
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "User information not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Close the activity on close icon click
        closeIcon.setOnClickListener(v -> finish());

        // Load user checkpoint data
        loadCheckpointData();

        // Open image in fullscreen dialog on click
        detailImage.setOnClickListener(v -> openImageDialog(imageUrl));
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailListActivity.this, ChatRoomActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("detailTitle", detailTitle.getText().toString()); // Pass the title correctly
                startActivity(intent);
            }
        });


    }

    private void loadCheckpointData() {
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username not available!", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(detailTitle.getText().toString()).child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Retrieve checkpoint values
                            Boolean checkpoint1 = snapshot.child("checkpoint1").getValue(Boolean.class);
                            Boolean checkpoint2 = snapshot.child("checkpoint2").getValue(Boolean.class);
                            Boolean checkpoint3 = snapshot.child("checkpoint3").getValue(Boolean.class);
                            Boolean eventCompleted = snapshot.child("eventcompleted").getValue(Boolean.class);

                            // Log fetched values for debugging purposes
                            Log.d("CheckpointData", "Checkpoint 1: " + checkpoint1);
                            Log.d("CheckpointData", "Checkpoint 2: " + checkpoint2);
                            Log.d("CheckpointData", "Checkpoint 3: " + checkpoint3);
                            Log.d("CheckpointData", "Event Completed: " + eventCompleted);

                            // Check and display the status of Checkpoint 1
                            if (checkpoint1 != null && checkpoint1) {
                                c1.setText("Checkpoint 1: Completed");
                            } else {
                                c1.setText("Checkpoint 1: Pending");
                            }

                            // Check and display the status of Checkpoint 2
                            if (checkpoint2 != null && checkpoint2) {
                                c2.setText("Checkpoint 2: Completed");
                            } else {
                                c2.setText("Checkpoint 2: Pending");
                            }

                            // Check and display the status of Checkpoint 3
                            if (checkpoint3 != null && checkpoint3) {
                                c3.setText("Checkpoint 3: Completed");
                            } else {
                                c3.setText("Checkpoint 3: Pending");
                            }

                            // Check and display the event status
                            if (eventCompleted != null && eventCompleted) {
                                status.setText("Status: Completed");
                            } else {
                                status.setText("Status: Event in Progress");
                            }
                        } else {
                            // Handle case where there is no checkpoint data
                            c1.setText("Checkpoint 1: Not Found");
                            c2.setText("Checkpoint 2: Not Found");
                            c3.setText("Checkpoint 3: Not Found");
                            status.setText("Status: Not Registered");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(DetailListActivity.this, "Error loading data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openImageDialog(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            Toast.makeText(this, "Image not available", Toast.LENGTH_SHORT).show();
            return;
        }

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fullscreen_image);

        ImageView fullScreenImage = dialog.findViewById(R.id.fullScreenImage);
        ImageView closeButton = dialog.findViewById(R.id.closeButton);

        // Load the image into the ImageView
        Glide.with(this).load(imageUrl).into(fullScreenImage);

        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}