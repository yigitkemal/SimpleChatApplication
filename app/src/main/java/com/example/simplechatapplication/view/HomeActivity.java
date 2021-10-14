package com.example.simplechatapplication.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.simplechatapplication.R;
import com.example.simplechatapplication.adapter.HomeRecyclerviewAdapter;
import com.example.simplechatapplication.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    HomeRecyclerviewAdapter homeRecyclerviewAdapter;

    private ArrayList<String> chatMessages = new ArrayList<>();

    private static final String ONESIGNAL_APP_ID = "7e777d90-968d-4237-948f-195709804a6a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        homeRecyclerviewAdapter = new HomeRecyclerviewAdapter(chatMessages);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(homeRecyclerviewAdapter);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://chatapplication-c841c-default-rtdb.europe-west1.firebasedatabase.app/");
        databaseReference = database.getReference();

        getData();


        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE,OneSignal.LOG_LEVEL.NONE);

        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_profile) {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        } else if (item.getItemId() == R.id.menu_signout) {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        } else {

        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view) {
        String messageToSend = binding.messageText.getText().toString();

        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        databaseReference.child("Chats")
                .child(uuidString)
                .child("usermessage")
                .setValue(messageToSend);

        databaseReference.child("Chats")
                .child(uuidString)
                .child("useremail")
                .setValue(mAuth.getCurrentUser().getEmail().toString());

        databaseReference.child("Chats")
                .child(uuidString)
                .child("usermessagetime")
                .setValue(ServerValue.TIMESTAMP);


        binding.messageText.setText("");
    }

    public void getData(){
        DatabaseReference newReference = database.getReference("Chats");

        Query query = newReference.orderByChild("usermessagetime");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatMessages.clear();

                for(DataSnapshot ds: snapshot.getChildren()){
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    String userEmail = hashMap.get("useremail");
                    String userMessage = hashMap.get("usermessage");
                    System.out.println(userMessage+"-----------------------------------------");
                    chatMessages.add(userEmail +" "+userMessage);

                    homeRecyclerviewAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

}