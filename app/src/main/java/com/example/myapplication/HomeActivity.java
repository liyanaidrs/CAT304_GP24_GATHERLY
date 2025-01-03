package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityHomeBinding;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);


        String username = getIntent().getStringExtra("username");
        replaceFragment(new HomeFragment(), username);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {

                replaceFragment(new HomeFragment(), username);
            } else if (itemId == R.id.mylist) {

                replaceFragment(new MyListFragment(), username);
            } else if (itemId == R.id.myreward) {
                replaceFragment(new MyRewardFragment(), username);
            } else if (itemId == R.id.profile) {

                replaceFragment(new ProfileFragment(), username);
            }

            return true;
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the username from the intent
                String username = getIntent().getStringExtra("username");

                if (username != null) {
                    // Pass the username to MyCodeFragment
                    MyCodeFragment fragment = new MyCodeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username); // Pass username
                    fragment.setArguments(bundle);
                    replaceFragment(fragment, username);
                }

                //showBottomDialog();
            }


        });

    }



    private void replaceFragment(Fragment fragment, String username) {
        // Pass the username to the fragment
        if (username != null) {
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            fragment.setArguments(bundle);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


//    private void showBottomDialog() {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.bottom_sheet_layout);
//
//        //LinearLayout QR = dialog.findViewById(R.id.QRcode);
//        LinearLayout Download = dialog.findViewById(R.id.Downloadqr);
//        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
//
////        QR.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                dialog.dismiss();
////                Toast.makeText(HomeActivity.this, "QR code is Clicked", Toast.LENGTH_SHORT).show();
////            }
////        });
//
//        Download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Toast.makeText(HomeActivity.this, "Download QR is Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//    }
}