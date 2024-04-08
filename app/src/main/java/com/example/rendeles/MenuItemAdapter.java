package com.example.rendeles;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private List<MenuItem> menuItemList;

    public MenuItemAdapter(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = menuItemList.get(position);
        holder.textViewName.setText(menuItem.getName());
        holder.textViewDescription.setText(menuItem.getDescription());
        holder.textViewPrice.setText(String.format("Ár: %s Ft", menuItem.getPrice()));
        holder.editTextQuantity.setText(String.valueOf(menuItem.getQuantity()));

        holder.editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    menuItem.setQuantity(Integer.parseInt(s.toString()));
                } catch (NumberFormatException e) {
                    menuItem.setQuantity(1); // Default or error value
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewDescription;
        TextView textViewPrice;
        EditText editTextQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            editTextQuantity = itemView.findViewById(R.id.editTextQuantity);
        }
    }
}
