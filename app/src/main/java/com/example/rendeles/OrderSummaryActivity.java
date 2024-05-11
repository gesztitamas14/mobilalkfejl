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
        String[] itemDetails = orderedItems.split("\n");
        double totalPrice = 0.0;

        for (String detail : itemDetails) {
            if (!detail.isEmpty()) {
                String[] parts = detail.split(", Ár: ");
                if (parts.length > 1) {
                    String pricePart = parts[1].split(" Ft")[0].trim();
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
                if (validateInputs()) {
                    EditText addressEditText = findViewById(R.id.addressEditText);
                    String address = addressEditText.getText().toString();

                    Intent intent = new Intent(OrderSummaryActivity.this, OrderConfirmationActivity.class);
                    intent.putExtra("USER_ADDRESS", address);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTemporaryData();
    }

    private void saveTemporaryData() {
        EditText phoneNumberEditText = findViewById(R.id.phoneEditText);
        EditText addressEditText = findViewById(R.id.addressEditText);
        String phoneNumber = phoneNumberEditText.getText().toString();
        String address = addressEditText.getText().toString();

        // Use SharedPreferences to save the data
        getSharedPreferences("OrderSummaryPrefs", MODE_PRIVATE)
                .edit()
                .putString("phoneNumber", phoneNumber)
                .putString("address", address)
                .apply();
    }

    private boolean validateInputs() {
        EditText phoneEditText = findViewById(R.id.phoneEditText);
        String phone = phoneEditText.getText().toString().trim();
        EditText nameEditText = findViewById(R.id.nameEditText);
        String name = nameEditText.getText().toString().trim();
        EditText addressEditText = findViewById(R.id.addressEditText);
        String address = addressEditText.getText().toString().trim();


        // Validate phone number
        if (phone.isEmpty() || phone.length() != 11) {
            phoneEditText.setError("Egy valós 11 számjegyű telefonszámot adj meg");
            return false;
        }

        // Validate name
        if (name.isEmpty()) {
            nameEditText.setError("Adj meg egy nevet!");
            return false;
        }

        // Validate address
        if (address.isEmpty()) {
            addressEditText.setError("Adj meg egy utcanevet!");
            return false;
        }

        // All inputs are valid
        return true;
    }
}


