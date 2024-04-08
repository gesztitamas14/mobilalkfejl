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

import java.util.ArrayList;
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
        Intent intent = new Intent(this, OrderSummaryActivity.class);
        intent.putExtra("ORDERED_ITEMS", orderData);
        startActivity(intent);
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
                            if (document.exists()) {
                                List<Map<String, Object>> menuItemsMaps = (List<Map<String, Object>>) document.get("MenuItems");
                                loadMenuItems(menuItemsMaps);
                            }
                        } else {
                            Log.e("MenuItemsActivity", "Error getting restaurant: ", task.getException());
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
        double price = ((Number) menuItemMap.get("price")).doubleValue();

        return new MenuItem(name, description, price);
    }
}
