package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.myapplication.databinding.ActivityAdminHomeBinding;

import android.os.Bundle;
import android.view.View;

public class AdminHomeActivity extends AppCompatActivity {

    ActivityAdminHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationViewAdmin.setBackground(null);

        replaceFragment(new AdminCreateFragment());

        binding.bottomNavigationViewAdmin.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.create) {
                replaceFragment(new AdminCreateFragment());
            } else if (itemId == R.id.adminlist) {
                replaceFragment(new AdminListFragment());
            } else if (itemId == R.id.adminstatistic) {
                replaceFragment(new AdminStatisticFragment());
            } else if (itemId == R.id.adminprofile) {
                replaceFragment(new AdminProfileFragment());
            }

            return true;
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replaceFragment(new AdminScanFragment());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout1, fragment);
        fragmentTransaction.commit();
    }
}
