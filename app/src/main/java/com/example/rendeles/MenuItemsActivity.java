package com.example.rendeles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MenuItemsActivity extends AppCompatActivity  {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private MenuItemAdapter adapter;
    private List<MenuItem> menuItemList;
    private Button submitOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.menuItemsRecyclerView);
        submitOrderButton = findViewById(R.id.submitOrderButton);
        menuItemList = new ArrayList<>();
        adapter = new MenuItemAdapter(menuItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        submitOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrderSubmission();
            }
        });

        String restaurantId = getIntent().getStringExtra("restaurantId");
        if (restaurantId != null) {
            loadRestaurantAndMenuItems(restaurantId);
        }
    }

    private void handleOrderSubmission() {
        String orderData = collectOrderData();
        if (!orderData.isEmpty()){
            Intent intent = new Intent(this, OrderSummaryActivity.class);
            intent.putExtra("ORDERED_ITEMS", orderData);
            startActivity(intent);
        }

    }

    private String collectOrderData() {
        StringBuilder orderData = new StringBuilder();
        for (MenuItem item : menuItemList) {
            if (item.getQuantity() > 0) {
                orderData.append(item.getName())
                        .append(" - Mennyiség: ")
                        .append(item.getQuantity())
                        .append(", Ár: ")
                        .append(item.getPrice())
                        .append(" Ft\n");
            }
        }
        return orderData.toString();
    }

    private void loadRestaurantAndMenuItems(String restaurantId) {
        db.collection("restaurants")
                .document(restaurantId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                List<Map<String, Object>> menuItemsMaps = (List<Map<String, Object>>) document.get("MenuItems");
                                if (menuItemsMaps != null) {
                                    // Convert each map to MenuItem object and collect to a list
                                    List<MenuItem> loadedMenuItems = new ArrayList<>();
                                    for (Map<String, Object> menuItemMap : menuItemsMaps) {
                                        MenuItem menuItem = convertMapToMenuItem(menuItemMap);
                                        if (menuItem != null) {
                                            loadedMenuItems.add(menuItem);
                                        }
                                    }
                                    // Sort the loaded menu items by price
                                    Collections.sort(loadedMenuItems, new Comparator<MenuItem>() {
                                        @Override
                                        public int compare(MenuItem o1, MenuItem o2) {
                                            return Double.compare(o1.getPrice(), o2.getPrice());
                                        }
                                    });
                                    // Clear old items and add all sorted items
                                    menuItemList.clear();
                                    menuItemList.addAll(loadedMenuItems);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.d("MenuItemsActivity", "No menu items found.");
                                }
                            } else {
                                Log.d("MenuItemsActivity", "No such document.");
                            }
                        } else {
                            Log.e("MenuItemsActivity", "Error getting document: ", task.getException());
                        }
                    }
                });
    }
    private void loadMenuItems(List<Map<String, Object>> menuItemsMaps) {
        if (menuItemsMaps != null) {
            for (Map<String, Object> menuItemMap : menuItemsMaps) {
                MenuItem menuItem = convertMapToMenuItem(menuItemMap);
                menuItemList.add(menuItem);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private MenuItem convertMapToMenuItem(Map<String, Object> menuItemMap) {
        String name = (String) menuItemMap.get("name");
        String description = (String) menuItemMap.get("description");
        double price = 0.0; // Initialize price

        Object priceObj = menuItemMap.get("price");
        if (priceObj instanceof Double) {
            price = (Double) priceObj;
        } else if (priceObj instanceof Long) {
            price = ((Long) priceObj).doubleValue(); // Convert Long to Double
        }

        MenuItem menuItem = new MenuItem(name, description, price);
        return menuItem;
    }

}
