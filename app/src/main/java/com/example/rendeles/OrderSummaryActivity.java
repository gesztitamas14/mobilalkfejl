package com.example.rendeles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OrderSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        String orderedItems = getIntent().getStringExtra("ORDERED_ITEMS");
        String[] itemDetails = orderedItems.split("\n"); // Split at each newline
        double totalPrice = 0.0;

        for (String detail : itemDetails) {
            if (!detail.isEmpty()) {
                String[] parts = detail.split(", Ár: "); // Split by ", Ár: "
                if (parts.length > 1) {
                    String pricePart = parts[1].split(" Ft")[0].trim(); // Get the part before " Ft"
                    try {
                        double price = Double.parseDouble(pricePart);
                        totalPrice += price;
                    } catch (NumberFormatException e) {
                        // Handle possible parsing errors or log them
                    }
                }
            }
        }

        TextView listOfOrderedItemsTextView = findViewById(R.id.listOfOrderedItemsTextView);
        TextView totalPriceTextView = findViewById(R.id.totalPriceTextView);

        listOfOrderedItemsTextView.setText(orderedItems);
        totalPriceTextView.setText("Összesített ár: " + String.format("%.2f Ft", totalPrice));

        Button confirmOrderButton = findViewById(R.id.confirmOrderButton);
        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addressEditText = findViewById(R.id.addressEditText);
                String address = addressEditText.getText().toString();

                Intent intent = new Intent(OrderSummaryActivity.this, OrderConfirmationActivity.class);
                intent.putExtra("USER_ADDRESS", address);
                startActivity(intent);
            }
        });
    }
}


