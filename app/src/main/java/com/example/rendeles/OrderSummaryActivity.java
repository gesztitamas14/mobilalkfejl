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

        TextView listOfOrderedItemsTextView = findViewById(R.id.listOfOrderedItemsTextView);
        listOfOrderedItemsTextView.setText(orderedItems);
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
