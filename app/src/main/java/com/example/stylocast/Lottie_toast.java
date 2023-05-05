package com.example.stylocast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
//import android.view.animation.AnimationUtils;

import com.airbnb.lottie.LottieAnimationView;

public class Lottie_toast extends AppCompatActivity {


    LottieAnimationView lottieAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie_toast);




//        lottieAnimation = findViewById(R.id.Toast_lottie);
        Intent toHome = new Intent(this,MainActivity.class);

        new Runnable() {
            @Override
            public void run() {

                lottieAnimation.setAnimation(R.raw.ok);
                lottieAnimation.playAnimation();
            }
        };


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(toHome);
            }
        },20000);

    }
}