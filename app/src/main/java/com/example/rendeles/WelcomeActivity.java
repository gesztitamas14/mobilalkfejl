package com.example.rendeles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        recyclerView = findViewById(R.id.restaurantsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(restaurantList);
        recyclerView.setAdapter(adapter);

        loadRestaurants();
    }

    private void loadRestaurants() {
        db.collection("restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        restaurantList.add(restaurant);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    // Hiba kezelése
                }
            }
        });
    }
}
