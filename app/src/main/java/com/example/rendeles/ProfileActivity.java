package com.example.rendeles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView emailTextView;
    private EditText newPasswordEditText;
    private Button changePasswordButton;
    private Button deleteAccountButton, backButton;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();

        emailTextView = findViewById(R.id.emailTextView);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);
        backButton = findViewById(R.id.backButton);


        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            emailTextView.setText(user.getEmail());
        }

        if (user != null) {
            emailTextView.setText(user.getEmail());
        }

        deleteAccountButton.setOnClickListener(v -> confirmAccountDeletion(user));
        backButton.setOnClickListener(v -> finish());

        changePasswordButton.setOnClickListener(v -> {
            String newPassword = newPasswordEditText.getText().toString().trim();
            if (!newPassword.isEmpty() && newPassword.length() >= 6) {
                updatePassword(user, newPassword);
            } else {
                Toast.makeText(ProfileActivity.this, "A jelszónak legalább 6 karakter hosszúnak kell lennie.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePassword(FirebaseUser user, String newPassword) {
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Jelszó sikeresen frissítve.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Jelszó frissítése sikertelen. Próbálja meg újra később.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Nem sikerült azonosítani a felhasználót.", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmAccountDeletion(FirebaseUser user) {
        new AlertDialog.Builder(this)
                .setTitle("Fiók törlése")
                .setMessage("Biztosan törölni szeretnéd a fiókodat? Ez a művelet nem visszavonható.")
                .setPositiveButton("Törlés", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUserAccount(user);
                    }
                })
                .setNegativeButton("Mégsem", null)
                .show();
    }

    private void deleteUserAccount(FirebaseUser user) {
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Fiók sikeresen törölve.", Toast.LENGTH_SHORT).show();
                            // Redirect to MainActivity
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // Finish all previous activities
                        } else {
                            Toast.makeText(ProfileActivity.this, "Fiók törlése sikertelen.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
