package com.example.tasky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class PinActivity extends AppCompatActivity implements View.OnClickListener {
    DBHelper dbHelper;

    private static String hard_coded_pin;

    Button btn1, btn2, btn3, btn4, btn5, btn6,
           btn7, btn8, btn9, btn0;

    Button back_arrow;

    ImageView pin1, pin2, pin3, pin4;

    Animation shake;

    static String test_pin = "";
    int num_of_passed_pins = 0;

    public static void setHardCodedPin(String new_hard_coded_pin){
        hard_coded_pin = new_hard_coded_pin;
    }

    public static String getHardCodedPin(){
        return hard_coded_pin;
    }

    @Override
    public void onClick(View v) {
        // default method for handling onClick Events...

        switch (v.getId()){
            case R.id.btn1:
                test_pin += "1";
                num_of_passed_pins++;
                break;

            case R.id.btn2:
                test_pin += "2";
                num_of_passed_pins++;
                break;

            case R.id.btn3:
                test_pin += "3";
                num_of_passed_pins++;
                break;

            case R.id.btn4:
                test_pin += "4";
                num_of_passed_pins++;
                break;

            case R.id.btn5:
                test_pin += "5";
                num_of_passed_pins++;
                break;

            case R.id.btn6:
                test_pin += "6";
                num_of_passed_pins++;
                break;

            case R.id.btn7:
                test_pin += "7";
                num_of_passed_pins++;
                break;

            case R.id.btn8:
                test_pin += "8";
                num_of_passed_pins++;
                break;

            case R.id.btn9:
                test_pin += "9";
                num_of_passed_pins++;
                break;

            case R.id.btn0:
                test_pin += "0";
                num_of_passed_pins++;
                break;

            case R.id.back_arrow:
                // remove the last character
                test_pin = test_pin.substring(0, test_pin.length()-1);
                num_of_passed_pins--;
                break;
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


        switch(num_of_passed_pins){
            case 0:
                pin1.setBackgroundResource(R.drawable.white_circle);
                pin2.setBackgroundResource(R.drawable.white_circle);
                pin3.setBackgroundResource(R.drawable.white_circle);
                pin4.setBackgroundResource(R.drawable.white_circle);

                break;

            case 1:
                pin1.setBackgroundResource(R.drawable.yellow_circle);
                pin2.setBackgroundResource(R.drawable.white_circle);
                pin3.setBackgroundResource(R.drawable.white_circle);
                pin4.setBackgroundResource(R.drawable.white_circle);

                break;

            case 2:
                pin1.setBackgroundResource(R.drawable.yellow_circle);
                pin2.setBackgroundResource(R.drawable.yellow_circle);
                pin3.setBackgroundResource(R.drawable.white_circle);
                pin4.setBackgroundResource(R.drawable.white_circle);

                break;

            case 3:
                pin1.setBackgroundResource(R.drawable.yellow_circle);
                pin2.setBackgroundResource(R.drawable.yellow_circle);
                pin3.setBackgroundResource(R.drawable.yellow_circle);
                pin4.setBackgroundResource(R.drawable.white_circle);

                break;

            case 4:
                pin1.setBackgroundResource(R.drawable.yellow_circle);
                pin2.setBackgroundResource(R.drawable.yellow_circle);
                pin3.setBackgroundResource(R.drawable.yellow_circle);
                pin4.setBackgroundResource(R.drawable.yellow_circle);

                if(hard_coded_pin.equals(test_pin)) {
                    // got to main_activity.xml
                    Intent main_activity = new Intent(PinActivity.this, MainActivity.class);
                    startActivity(main_activity);
                    finish();
                }

                // if the pin was wrong
                else{
                    num_of_passed_pins = 0;
                    test_pin = "";

                    pin1.startAnimation(shake);
                    pin2.startAnimation(shake);
                    pin3.startAnimation(shake);
                    pin4.startAnimation(shake);

                    pin1.setBackgroundResource(R.drawable.white_circle);
                    pin2.setBackgroundResource(R.drawable.white_circle);
                    pin3.setBackgroundResource(R.drawable.white_circle);
                    pin4.setBackgroundResource(R.drawable.white_circle);
                }

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        dbHelper = new DBHelper(PinActivity.this);

        hard_coded_pin = dbHelper.getPassword();

        pin1 = findViewById(R.id.pin_1);
        pin2 = findViewById(R.id.pin_2);
        pin3 = findViewById(R.id.pin_3);
        pin4 = findViewById(R.id.pin_4);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);

        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(this);

        btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(this);

        btn4 = findViewById(R.id.btn4);
        btn4.setOnClickListener(this);

        btn5 = findViewById(R.id.btn5);
        btn5.setOnClickListener(this);

        btn6 = findViewById(R.id.btn6);
        btn6.setOnClickListener(this);

        btn7 = findViewById(R.id.btn7);
        btn7.setOnClickListener(this);

        btn8 = findViewById(R.id.btn8);
        btn8.setOnClickListener(this);

        btn9 = findViewById(R.id.btn9);
        btn9.setOnClickListener(this);

        btn0 = findViewById(R.id.btn0);
        btn0.setOnClickListener(this);

        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);

        shake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);

    }
}
