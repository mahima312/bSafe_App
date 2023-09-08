package com.mahima.bsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.play.core.integrity.v;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mahima.bsafe.databinding.ActivityNumberBinding;

public class NumberActivity extends AppCompatActivity {

        ActivityNumberBinding binding;
        FirebaseAuth firebaseAuth;
        FirebaseDatabase firebaseDatabase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            binding = ActivityNumberBinding.inflate(getLayoutInflater());

            setContentView(binding.getRoot());

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();

            binding.btnFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String numberString = binding.etEgyMobileNo.getText().toString();
                    if (numberString.length() == 10) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("ENUM", numberString);
                        myEdit.apply();
                        NumberActivity.this.finish();
                        Toast.makeText(NumberActivity.this, "Welcome ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NumberActivity.this,SendSosActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(NumberActivity.this, "Enter Valid Number!", Toast.LENGTH_SHORT).show();
                    }



                }
            });


        }
}
