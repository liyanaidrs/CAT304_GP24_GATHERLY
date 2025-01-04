package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText adminEmail, adminPassword;
    private Button adminLoginButton, userLoginButton, organiserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Assuming your XML file is named activity_main.xml

        adminEmail = findViewById(R.id.admin_email);
        adminPassword = findViewById(R.id.admin_password);
        adminLoginButton = findViewById(R.id.Admin_button);
        userLoginButton = findViewById(R.id.LOGIN_button);
        organiserButton=findViewById(R.id.Organiser_button);

        // Set up the admin login button
        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = adminEmail.getText().toString().trim();
                String password = adminPassword.getText().toString().trim();

                // Check if credentials match the admin's credentials
                if (email.equals("admin1@gmail.com") && password.equals("@123456")) {
                    // Navigate to LoginActivity if admin login is successful
                    Intent intent = new Intent(MainActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                    finish(); // Close MainActivity so that the user can't go back to it
                } else {
                    // Show an error message if credentials are incorrect
                    Toast.makeText(MainActivity.this, "Invalid admin credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up the user login button
        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity for regular users
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        organiserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity for regular users
                Intent intent = new Intent(MainActivity.this, OrganiserActivity.class);
                startActivity(intent);
            }
        });

    }
}