package com.example.simplechatapplication.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.simplechatapplication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view  = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null)
            startActivity(new Intent(MainActivity.this,HomeActivity.class));

    }


    //firebase methodları
    public void signIn(View view){
        mAuth.signInWithEmailAndPassword(
                binding.userEmailEdittext.getText().toString(),
                binding.userPasswordEdittext.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"signInWithEmailAndPassword:Success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Log.d(TAG,"signInWithEmailAndPassword:Failure");
                        }
                    }
                });
    }

    public void signUp(View view){
        mAuth.createUserWithEmailAndPassword(
                binding.userEmailEdittext.getText().toString(),
                binding.userPasswordEdittext.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"createUserWithEmail:Success");
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d(TAG,"createUserWithEmail:failure");
                    Toast.makeText(MainActivity.this, "Sign Up Failed!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    //firebase methodları


    public void updateUI(FirebaseUser user){
            Toast.makeText(this,"You Signed In successfully",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);

    }

}