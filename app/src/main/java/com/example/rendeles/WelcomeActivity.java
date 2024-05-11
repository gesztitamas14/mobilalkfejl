package com.example.rendeles;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Address;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;


import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private List<Restaurant> restaurantList;
    private ImageView profileIcon;
    private FirebaseAuth mAuth;

    private FusedLocationProviderClient locationClient;
    private String userCity;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        recyclerView = findViewById(R.id.restaurantsRecyclerView);
        profileIcon = findViewById(R.id.profileIcon);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        adapter = new RestaurantAdapter(restaurantList);
        recyclerView.setAdapter(adapter);

        //loadRestaurants();
        loadProfileIcon();

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationAccess();
    }

    private void requestLocationAccess() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            getLastLocation();
        }
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        locationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            determineCity(location);
                            // Move the call to loadRestaurants here, once city is determined
                            loadRestaurants();
                        } else {
                            Log.e("LocationError", "Location is null");
                        }
                    }
                });
    }


    private void determineCity(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                userCity = address.getLocality();
                // Since city is determined here, you might want to update related UI or logic here as well
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProfileIcon() {
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/mobilalk-8e79b.appspot.com/o/profile.png?alt=media&token=6b50bf12-e11d-4d94-b634-f9e7d9c4a493";
        Glide.with(this)
                .load(imageUrl)
                .into(profileIcon);
    }

    private void loadRestaurants() {
        db.collection("restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean deliveryAvailable = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        restaurantList.add(restaurant);
                        if (restaurant != null && restaurant.getAddress() != null && userCity != null) {
                            if (restaurant.getAddress().contains(userCity)) {
                                deliveryAvailable = true;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    TextView tvDeliveryInfo = findViewById(R.id.tvDeliveryInfo);
                    if (!deliveryAvailable && userCity != null) {
                        tvDeliveryInfo.setVisibility(View.VISIBLE);
                    } else if (tvDeliveryInfo != null) {
                        tvDeliveryInfo.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("FirestoreError", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    private void loadRestaurant() {
        db.collection("restaurants").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean deliveryAvailable = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Restaurant restaurant = document.toObject(Restaurant.class);
                        restaurantList.add(restaurant);
                        if (restaurant != null && restaurant.getAddress() != null && userCity != null) {
                            if (restaurant.getAddress().contains(userCity)) {
                                deliveryAvailable = true;
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    TextView tvDeliveryInfo = findViewById(R.id.tvDeliveryInfo);
                    if (!deliveryAvailable && userCity != null) {
                        tvDeliveryInfo.setVisibility(View.VISIBLE);
                    } else if (tvDeliveryInfo != null) {
                        tvDeliveryInfo.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("FirestoreError", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(); // Call this to get location and update restaurants list
            } else {
                Toast.makeText(this, "Lokáció engedélyezése szükséges a helyi éttermek megjelenítéséhez.", Toast.LENGTH_LONG).show();
                // Consider updating UI to reflect that location-based filtering isn't available
                loadRestaurants(); // Load restaurants without location filtering
            }
        }
    }
}
