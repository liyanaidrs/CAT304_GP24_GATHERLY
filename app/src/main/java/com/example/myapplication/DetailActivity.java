package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang, detailDate, detailParticipant, c1, c2, c3;
    ImageView detailImage;
    Button deleteButton;
    Button editButton;
    String key = "";
    String imageUrl = "";
    Button chat;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailLang = findViewById(R.id.detailLang);
        detailDate = findViewById(R.id.detailDate);
        detailParticipant = findViewById(R.id.detailParticipant);
        ImageView closeIcon = findViewById(R.id.cancelButton);
        chat=findViewById(R.id.chatbutton);
        deleteButton = findViewById(R.id.deletebutton);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);

        // Initialize Firebase references
        databaseReference = FirebaseDatabase.getInstance().getReference("Event Created");
        storageReference = FirebaseStorage.getInstance().getReference("Event Images");

        // Retrieve event details from Intent
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
        }

        // Open the image in fullscreen when clicked
        detailImage.setOnClickListener(v -> openImageDialog(imageUrl));
        closeIcon.setOnClickListener(v -> finish());
        // Close the DetailActivity and return to the previous activity

        // Handle delete button click
        deleteButton.setOnClickListener(v -> {
            if (key != null && !key.isEmpty()) {
                // First, delete the image from Firebase Storage
//                StorageReference imageRef = storageReference;
//                imageRef.delete().addOnSuccessListener(unused -> {
                // If the image deletion is successful, proceed to delete the event data
                databaseReference.child(key).removeValue().addOnSuccessListener(unused1 -> {
                    Toast.makeText(DetailActivity.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                    // Close the activity and return to the previous screen
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(DetailActivity.this, "Failed to delete event data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
//                }).addOnFailureListener(e -> {
//                    Toast.makeText(DetailActivity.this, "Failed to delete image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
            } else {
                Toast.makeText(DetailActivity.this, "Event key not available", Toast.LENGTH_SHORT).show();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, AdminChatActivity.class);
                startActivity(intent);
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