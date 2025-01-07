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

        adminLoginButton = findViewById(R.id.Admin_button);
        userLoginButton = findViewById(R.id.LOGIN_button);
        organiserButton=findViewById(R.id.Organiser_button);

        // Set up the admin login button
        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity for regular users
                Intent intent = new Intent(MainActivity.this, LoginAdminActivity.class);
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

        // Set up the user login button
        userLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginActivity for regular users
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}