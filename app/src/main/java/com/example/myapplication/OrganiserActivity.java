package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OrganiserActivity extends AppCompatActivity {

    private EditText organiserEmail, organiserPassword;
    private Button organiserLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser); // Assuming your XML file is named activity_main.xml

        organiserEmail = findViewById(R.id.login_email);
        organiserPassword = findViewById(R.id.login_password);
        organiserLoginButton = findViewById(R.id.login_button);


        // Set up the admin login button
        organiserLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = organiserEmail.getText().toString().trim();
                String password = organiserPassword.getText().toString().trim();

                // Check if credentials match the organiser's credentials
                if (email.equals("organiser1@gmail.com") && password.equals("@123456")) {
                    // Navigate to LoginActivity if admin login is successful
                    Intent intent = new Intent(OrganiserActivity.this, OrganiserScanActivity.class);
                    startActivity(intent);
                    finish(); // Close MainActivity so that the user can't go back to it
                } else {
                    // Show an error message if credentials are incorrect
                    Toast.makeText(OrganiserActivity.this, "Invalid organiser credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}