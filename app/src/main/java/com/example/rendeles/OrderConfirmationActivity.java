package com.example.rendeles;

import com.bumptech.glide.Glide;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;

public class OrderConfirmationActivity extends AppCompatActivity {

    private ImageView carImageView;
    private ConstraintLayout constraintLayout;
    private TextView restaurantNameTextView, addressTextView;
    private View lineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        carImageView = findViewById(R.id.carImageView);
        constraintLayout = findViewById(R.id.constraintLayout);
        restaurantNameTextView = findViewById(R.id.restaurantNameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        lineView = findViewById(R.id.lineView);

        String userAddress = getIntent().getStringExtra("USER_ADDRESS");
        addressTextView.setText(userAddress);

        loadCarImage();
        startCarAnimation();
    }

    private void loadCarImage() {
        String imageUrl = "https://firebasestorage.googleapis.com/v0/b/mobilalk-8e79b.appspot.com/o/cars%2Fcar_icon.png?alt=media&token=d5547288-a704-4cbd-8749-5e4a0bd49f6b";
        Glide.with(this)
                .load(imageUrl)
                .into(carImageView);
    }

    private void startCarAnimation() {
        // Wait until layout is done
        carImageView.post(new Runnable() {
            @Override
            public void run() {
                carImageView.setVisibility(View.VISIBLE);

                // Get the positions relative to the screen
                int[] lineStart = new int[2];
                lineView.getLocationInWindow(lineStart);
                int[] lineEnd = new int[2];
                addressTextView.getLocationInWindow(lineEnd);

                // Calculate start and end positions for the animation
                float startX = lineStart[0];
                float endX = lineEnd[0] - carImageView.getWidth(); // Subtract car width to keep it in screen

                // Set up and start the animation
                ObjectAnimator animatorX = ObjectAnimator.ofFloat(carImageView, "x", startX, endX);
                animatorX.setDuration(10000); // 10 seconds

                // Animation Listener
                animatorX.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Popup message
                        new AlertDialog.Builder(OrderConfirmationActivity.this)
                                .setMessage("Szállítmányod megérkezett, jó étvágyat!")
                                .setPositiveButton("OK", (dialog, which) -> redirectToWelcomeActivity())
                                .setCancelable(false)
                                .show();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

                animatorX.start();
            }
        });
    }

    private void redirectToWelcomeActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish(); // To prevent returning to this activity
    }


}
