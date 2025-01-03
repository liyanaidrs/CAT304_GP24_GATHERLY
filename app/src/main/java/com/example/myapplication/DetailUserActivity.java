package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.bumptech.glide.Glide;

public class DetailUserActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang, detailDate, detailParticipant, c1, c2, c3;
    ImageView detailImage;
    String key = "";
    String imageUrl = "";
    Button apply;
    String username;
    private DatabaseReference databaseReference, usereventdb, userEventApplicationsRef, checkpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        // Initialize UI components
        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailLang = findViewById(R.id.detailLang);
        detailDate = findViewById(R.id.detailDate);
        detailParticipant = findViewById(R.id.detailParticipant);
        ImageView closeIcon = findViewById(R.id.cancelButton);
        apply = findViewById(R.id.applybutton);
        c1=findViewById(R.id.c1);
        c2=findViewById(R.id.c2);
        c3=findViewById(R.id.c3);

        // Firebase initialization
        databaseReference = FirebaseDatabase.getInstance().getReference("Applications");
        usereventdb = FirebaseDatabase.getInstance().getReference("UserApplication");
        userEventApplicationsRef = FirebaseDatabase.getInstance().getReference("UserEventApplications");
        checkpoints= FirebaseDatabase.getInstance().getReference("EventCheckpoints");
        // Retrieve event details from intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            detailDate.setText(bundle.getString("Date"));
            detailParticipant.setText(bundle.getString("Participant"));
            c1.setText(bundle.getString("c1"));
            c2.setText(bundle.getString("c2"));
            c3.setText(bundle.getString("c3"));
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
            username = bundle.getString("currentUser");
        }

        // Handle missing user case
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "User information not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Open image dialog on image click
        detailImage.setOnClickListener(v -> openImageDialog(imageUrl));

        // Close the activity on close icon click
        closeIcon.setOnClickListener(v -> finish());

        // Apply button functionality
        apply.setOnClickListener(v -> saveApplication());
    }

    private void saveApplication() {
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "You must be logged in to apply!", Toast.LENGTH_SHORT).show();
            return;
        }

        usereventdb.child(username).child(key).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                apply.setEnabled(false);
                apply.setText("You have already applied");
                Toast.makeText(DetailUserActivity.this, "You have already applied for this event!", Toast.LENGTH_SHORT).show();
            } else {
                ApplicationData applicationData = new ApplicationData(
                        username,
                        imageUrl,
                        detailTitle.getText().toString(),
                        detailDesc.getText().toString(),
                        detailLang.getText().toString(),
                        detailDate.getText().toString(),
                        key
                        ,c1.getText().toString(),c2.getText().toString(),c3.getText().toString()
                );

                EventCheckpoints eventCheckpoints = new EventCheckpoints(
                        username,
                        key,
                        detailTitle.getText().toString()
                );

                String applicationId = databaseReference.push().getKey();
                if (applicationId == null) {
                    Toast.makeText(DetailUserActivity.this, "Error generating application ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseReference.child(applicationId).setValue(applicationData)
                        .addOnSuccessListener(aVoid -> {
                            usereventdb.child(username).child(key).setValue(true)
                                    .addOnSuccessListener(aVoid1 -> {
                                        userEventApplicationsRef.child(key).child(username).setValue(true)
                                                .addOnSuccessListener(aVoid2 -> {
                                                    checkpoints.child(detailTitle.getText().toString()).child(username).setValue(eventCheckpoints)
                                                            .addOnSuccessListener(aVoid3 -> {
                                                                Toast.makeText(DetailUserActivity.this, "Applied successfully!", Toast.LENGTH_SHORT).show();
                                                                apply.setEnabled(false);
                                                                apply.setText("You have already applied");
                                                                finish();
                                                            })
                                                            .addOnFailureListener(e -> handleFailure("Saving checkpoint data", e));
                                                })
                                                .addOnFailureListener(e -> handleFailure("Saving event application relationship", e));
                                    })
                                    .addOnFailureListener(e -> handleFailure("Saving user application", e));
                        })
                        .addOnFailureListener(e -> handleFailure("Saving application data", e));
            }
        });
    }

    private void handleFailure(String operation, Exception e) {
        Toast.makeText(DetailUserActivity.this, "Failed to apply during " + operation + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
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