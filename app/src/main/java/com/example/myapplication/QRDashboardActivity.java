package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRDashboardActivity extends AppCompatActivity {

    private EditText usernameInput;
    private ImageView qrImageView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdashboard);

        usernameInput = findViewById(R.id.usernameInput);
        qrImageView = findViewById(R.id.qr);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        findViewById(R.id.generateQRbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRCode();
            }
        });

        ImageView closeIcon = findViewById(R.id.cancelButton);
        closeIcon.setOnClickListener(v -> finish());
    }

    private void generateQRCode() {
        String username = usernameInput.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if username exists in the database
        databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Username exists, generate QR code
                    try {
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.encodeBitmap(username, BarcodeFormat.QR_CODE, 400, 400);
                        qrImageView.setImageBitmap(bitmap);
                        Toast.makeText(QRDashboardActivity.this, "QR Code generated successfully", Toast.LENGTH_SHORT).show();
                    } catch (WriterException e) {
                        e.printStackTrace();
                        Toast.makeText(QRDashboardActivity.this, "Error generating QR Code", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Username does not exist
                    Toast.makeText(QRDashboardActivity.this, "Username does not exist in the database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(QRDashboardActivity.this, "Error checking username: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}