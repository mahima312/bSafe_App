package com.mahima.bsafe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.mahima.bsafe.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

            ActivityRegisterBinding binding;
            FirebaseAuth firebaseAuth;
            FirebaseDatabase firebaseDatabase;
            FirebaseStorage firebaseStorage;

            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (resultCode == Activity.RESULT_OK){
                    Uri uri = data.getData();

                    binding.ivDP.setImageURI(uri);


                    uploadImage(uri);
                }
            }

            private void uploadImage(Uri uri) {

                firebaseStorage.getReference()
                        .child("dp")
                        .putFile(uri)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();


                                } else{
                                    Toast.makeText(RegisterActivity.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                binding = ActivityRegisterBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());

                firebaseAuth = FirebaseAuth.getInstance();

                firebaseDatabase = FirebaseDatabase.getInstance();
                firebaseStorage = FirebaseStorage.getInstance();



                binding.ivDP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImagePicker.with(RegisterActivity.this)
                                .crop()
                                .compress(1024)
                                .maxResultSize(1080, 1080)
                                .start();
                    }
                });

                binding.btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String email = binding.etEmail.getText().toString();
                        String password = binding.etPassword.getText().toString();
                        String name = binding.etName.getText().toString();
                        String mobileNo = binding.etMoblie.getText().toString();
                        String city = binding.etCity.getText().toString();

                        binding.pbLoading.setVisibility(view.VISIBLE);

                        firebaseAuth.createUserWithEmailAndPassword(
                                email,
                                password
                        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                    saveUser(email, password, name, mobileNo, city);
                                }else{
                                    Toast.makeText(RegisterActivity.this, "User not created", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }

            private void saveUser(String email, String password, String name, String mobileNo, String city) {
                String uuid = firebaseAuth.getCurrentUser().getUid();

                UserData data = new UserData(uuid, name,  email, city,mobileNo, password);
                firebaseDatabase.getReference()
                        .child("UserData")
                        .child(uuid)
                        .setValue(data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "User saved", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(RegisterActivity.this, "User not save", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        }
