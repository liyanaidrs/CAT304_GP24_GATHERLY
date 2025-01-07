package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginAdminActivity extends AppCompatActivity {

    private EditText adminEmail, adminPassword;
    private Button adminLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        adminEmail = findViewById(R.id.admin_email);
        adminPassword = findViewById(R.id.admin_password);
        adminLoginButton = findViewById(R.id.login_button);

        // Set up the admin login button
        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = adminEmail.getText().toString().trim();
                String password = adminPassword.getText().toString().trim();

                // Check if credentials match the admin's credentials
                if (email.equals("admin1@gmail.com") && password.equals("@123456")) {
                    // Navigate to LoginActivity if admin login is successful
                    Intent intent = new Intent(LoginAdminActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                    finish(); // Close MainActivity so that the user can't go back to it
                } else {
                    // Show an error message if credentials are incorrect
                    Toast.makeText(LoginAdminActivity.this, "Invalid admin credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}