package com.example.tasky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    static int splash_timeout = 4000;
//    Animation text_blink_anim;
    Animation vintage_rotate_anim;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LinearLayout frameLayout = findViewById(R.id.splash_bg);
        animationDrawable = (AnimationDrawable)frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

//        text_blink_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.text_slpash_animation);
        vintage_rotate_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.vintage_splash_animation);

//        LinearLayout linearLayout = findViewById(R.id.splash_tasky);
//        linearLayout.startAnimation(text_blink_anim);

        ImageView imageView = findViewById(R.id.vintage);
        imageView.startAnimation(vintage_rotate_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash_activity = new Intent(SplashActivity.this, PinActivity.class);
                startActivity(splash_activity);
                finish();
            }
        }, splash_timeout);
    }

}
