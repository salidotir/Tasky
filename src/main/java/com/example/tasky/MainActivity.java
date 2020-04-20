package com.example.tasky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ChangePasswordDialog.ChangePasswordDialogListener {
    DBHelper dbHelper;

    FragmentTransaction transaction;

    HorizontalScrollView scrollView;

    Button btn_today;
    Button btn_previously_selected;
    int diff_day;

    static int today_value;
    static int day_week;        // the no. for the day week selected on top
                                // {(SAT : 0), (SUN : 1), (MON : 2), (TUE : 3), (WED : 4), (THU : 5), (FRI :6)}

    public static int last_id;

    int saturday_value, sunday_value, monday_value, tuesday_value, wednesday_value, thursday_value, friday_value;
    Button btn_saturday, btn_sunday, btn_monday, btn_tuesday, btn_wednesday, btn_thursday, btn_friday;

    AlphaAnimation buttonClick;

    LinearLayout menu_item;
    FrameLayout bottom_menu_with_alpha;
    Button btn_menu;
    Button btn_change_password;

    Animation open_menu;
    Animation close_menu;

    Button btn_history;

    @Override
    public void applyPasswordChange(String old_pass, String new_pass1, String new_pass2) {
        if(old_pass.equals(PinActivity.getHardCodedPin())) {
            if (new_pass1.equals(new_pass2)) {
                boolean res = dbHelper.setPassword(new_pass1);
                if (res == true) {
                    Toast.makeText(getApplicationContext(), "Password changed successfully.", Toast.LENGTH_SHORT).show();
                } else if (res == false) {
                    Toast.makeText(getApplicationContext(), "Could not change password in data base!.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Could not change password! Wrong password.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Could not change password! New passwords did not match.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateDatabaseIfWeekChanged(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int current_week_no = calendar.WEEK_OF_YEAR;
        int saved_week_no = dbHelper.getWeekSaved();

        if(current_week_no != saved_week_no){
            dbHelper.reUpdateDatabase();
            dbHelper.setWeekSaved(current_week_no);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this);

        last_id = dbHelper.getLastIdInserted();

//        updateDatabaseIfWeekChanged();

        // set a new today_fragment on layout at first
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment, new TodayFragment());
        transaction.commit();

        // define scroll view
        scrollView = findViewById(R.id.scrollView);
        scrollView.smoothScrollBy(2000, 0);

        // define menu
        bottom_menu_with_alpha = findViewById(R.id.bottom_menu_with_alpha);

        // define menu buttons
        btn_menu = findViewById(R.id.btn_menu);
        menu_item = findViewById(R.id.menu_item);
        btn_menu.setOnClickListener(this);

        btn_change_password = findViewById(R.id.btn_change_password);
        btn_change_password.setOnClickListener(this);

        btn_history = findViewById(R.id.btn_history);
        btn_history.setOnClickListener(this);

        // define menu animations
        open_menu = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.open_menu);
        close_menu = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.close_menu);

        // button click animation
        buttonClick = new AlphaAnimation(1F, 0.8F);

        // define week's days buttons
        btn_saturday = findViewById(R.id.saturday);
        saturday_value = 1;
        btn_saturday.setOnClickListener(this);

        btn_sunday = findViewById(R.id.sunday);
        sunday_value = 2;
        btn_sunday.setOnClickListener(this);

        btn_monday = findViewById(R.id.monday);
        monday_value = 3;
        btn_monday.setOnClickListener(this);

        btn_tuesday = findViewById(R.id.tuesday);
        tuesday_value = 4;
        btn_tuesday.setOnClickListener(this);

        btn_wednesday = findViewById(R.id.wednesday);
        wednesday_value = 5;
        btn_wednesday.setOnClickListener(this);

        btn_thursday = findViewById(R.id.thursday);
        thursday_value = 6;
        btn_thursday.setOnClickListener(this);

        btn_friday = findViewById(R.id.friday);
        friday_value = 7;
        btn_friday.setOnClickListener(this);

        // get current day from Calendar class
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day){
            case (Calendar.SATURDAY):
                btn_today = findViewById(R.id.saturday);
                btn_today.setText("Today");
                btn_today.setTextColor(Color.WHITE);
                btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);

                day_week = 0;
                today_value = saturday_value;
                btn_previously_selected = btn_today;
                break;

            case (Calendar.SUNDAY):
                btn_today = findViewById(R.id.sunday);
                btn_today.setText("Today");
                btn_today.setTextColor(Color.WHITE);
                btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);

                day_week = 1;
                today_value = sunday_value;
                btn_previously_selected = btn_today;
                break;

            case (Calendar.MONDAY):
                btn_today = findViewById(R.id.monday);
                btn_today.setText("Today");
                btn_today.setTextColor(Color.WHITE);
                btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);

                day_week = 2;
                today_value = monday_value;
                btn_previously_selected = btn_today;
                break;

            case (Calendar.TUESDAY):
                btn_today = findViewById(R.id.tuesday);
                btn_today.setText("Today");
                btn_today.setTextColor(Color.WHITE);
                btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);

                day_week = 3;
                today_value = tuesday_value;
                btn_previously_selected = btn_today;
                break;

            case (Calendar.WEDNESDAY):
                btn_today = findViewById(R.id.wednesday);
                btn_today.setText("Today");
                btn_today.setTextColor(Color.WHITE);
                btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);

                day_week = 4;
                today_value = wednesday_value;
                btn_previously_selected = btn_today;
                break;

            case (Calendar.THURSDAY):
                btn_today = findViewById(R.id.thursday);
                btn_today.setText("Today");
                btn_today.setTextColor(Color.WHITE);
                btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);

                day_week = 5;
                today_value = thursday_value;
                btn_previously_selected = btn_today;
                break;

            case (Calendar.FRIDAY):
                btn_today = findViewById(R.id.friday);
                btn_today.setText("Today");
                btn_today.setTextColor(Color.WHITE);
                btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);

                day_week = 6;
                today_value = friday_value;
                btn_previously_selected = btn_today;
                break;
        }

    }

    @Override
    public void onClick(View v) {
        // default method for handling onClick Events...
        switch (v.getId()){
            case R.id.saturday:
                btn_saturday.startAnimation(buttonClick);
                btn_today = findViewById(R.id.saturday);
                if(btn_today.equals(btn_previously_selected) == false){
                    btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);
                    btn_today.setTextColor(Color.WHITE);
                    btn_previously_selected.setBackgroundResource(R.drawable.round_white_rectangle);
                    btn_previously_selected.setTextColor(Color.BLACK);
                    btn_previously_selected = btn_today;
                }

                day_week = 0;
                diff_day = today_value - saturday_value;
                if(diff_day > 0){

                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new PreviousDayFragment());
                    transaction.commit();
                }
                else if(diff_day < 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new NextDayFragment());
                    transaction.commit();
                }
                else{
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new TodayFragment());
                    transaction.commit();
                }
                break;

            case R.id.sunday:
                btn_sunday.startAnimation(buttonClick);
                btn_today = findViewById(R.id.sunday);
                if(btn_today.equals(btn_previously_selected) == false){
                    btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);
                    btn_today.setTextColor(Color.WHITE);
                    btn_previously_selected.setBackgroundResource(R.drawable.round_white_rectangle);
                    btn_previously_selected.setTextColor(Color.BLACK);
                    btn_previously_selected = btn_today;
                }

                day_week = 1;
                diff_day = today_value - sunday_value;
                if(diff_day > 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new PreviousDayFragment());
                    transaction.commit();
                }
                else if(diff_day < 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new NextDayFragment());
                    transaction.commit();
                }
                else{
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new TodayFragment());
                    transaction.commit();
                }
                break;

            case R.id.monday:
                btn_monday.startAnimation(buttonClick);
                btn_today = findViewById(R.id.monday);
                if(btn_today.equals(btn_previously_selected) == false){
                    btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);
                    btn_today.setTextColor(Color.WHITE);
                    btn_previously_selected.setBackgroundResource(R.drawable.round_white_rectangle);
                    btn_previously_selected.setTextColor(Color.BLACK);
                    btn_previously_selected = btn_today;
                }

                day_week = 2;
                diff_day = today_value - monday_value;
                if(diff_day > 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new PreviousDayFragment());
                    transaction.commit();
                }
                else if(diff_day < 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new NextDayFragment());
                    transaction.commit();
                }
                else{
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new TodayFragment());
                    transaction.commit();
                }
                break;

            case R.id.tuesday:
                btn_tuesday.startAnimation(buttonClick);
                btn_today = findViewById(R.id.tuesday);
                if(btn_today.equals(btn_previously_selected) == false){
                    btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);
                    btn_today.setTextColor(Color.WHITE);
                    btn_previously_selected.setBackgroundResource(R.drawable.round_white_rectangle);
                    btn_previously_selected.setTextColor(Color.BLACK);
                    btn_previously_selected = btn_today;
                }

                day_week = 3;
                diff_day = today_value - tuesday_value;
                if(diff_day > 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new PreviousDayFragment());
                    transaction.commit();
                }
                else if(diff_day < 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new NextDayFragment());
                    transaction.commit();
                }
                else{
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new TodayFragment());
                    transaction.commit();
                }
                break;

            case R.id.wednesday:
                btn_wednesday.startAnimation(buttonClick);
                btn_today = findViewById(R.id.wednesday);
                if(btn_today.equals(btn_previously_selected) == false){
                    btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);
                    btn_today.setTextColor(Color.WHITE);
                    btn_previously_selected.setBackgroundResource(R.drawable.round_white_rectangle);
                    btn_previously_selected.setTextColor(Color.BLACK);
                    btn_previously_selected = btn_today;
                }

                day_week = 4;
                diff_day = today_value - wednesday_value;
                if(diff_day > 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new PreviousDayFragment());
                    transaction.commit();
                }
                else if(diff_day < 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new NextDayFragment());
                    transaction.commit();
                }
                else{
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new TodayFragment());
                    transaction.commit();
                }
                break;

            case R.id.thursday:
                btn_thursday.startAnimation(buttonClick);
                btn_today = findViewById(R.id.thursday);
                if(btn_today.equals(btn_previously_selected) == false){
                    btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);
                    btn_today.setTextColor(Color.WHITE);
                    btn_previously_selected.setBackgroundResource(R.drawable.round_white_rectangle);
                    btn_previously_selected.setTextColor(Color.BLACK);
                    btn_previously_selected = btn_today;
                }

                day_week = 5;
                diff_day = today_value - thursday_value;
                if(diff_day > 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new PreviousDayFragment());
                    transaction.commit();
                }
                else if(diff_day < 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new NextDayFragment());
                    transaction.commit();
                }
                else{
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new TodayFragment());
                    transaction.commit();
                }
                break;

            case R.id.friday:
                btn_friday.startAnimation(buttonClick);
                btn_today = findViewById(R.id.friday);
                if(btn_today.equals(btn_previously_selected) == false){
                    btn_today.setBackgroundResource(R.drawable.round_gradient_rectangle);
                    btn_today.setTextColor(Color.WHITE);
                    btn_previously_selected.setBackgroundResource(R.drawable.round_white_rectangle);
                    btn_previously_selected.setTextColor(Color.BLACK);
                    btn_previously_selected = btn_today;
                }

                day_week = 6;
                diff_day = today_value - friday_value;
                if(diff_day > 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new PreviousDayFragment());
                    transaction.commit();
                }
                else if(diff_day < 0){
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new NextDayFragment());
                    transaction.commit();
                }
                else{
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, new TodayFragment());
                    transaction.commit();
                }
                break;

            //~~~~~~~~~~~~~~~~~

            case R.id.btn_menu:
                btn_menu.startAnimation(buttonClick);
                if(menu_item.getVisibility() == View.VISIBLE){
                    menu_item.startAnimation(close_menu);
                    btn_menu.setRotation(0);
                    menu_item.setVisibility(View.INVISIBLE);
                    bottom_menu_with_alpha.setVisibility(View.INVISIBLE);
                }

                else if(menu_item.getVisibility() == View.INVISIBLE){
                    bottom_menu_with_alpha.setVisibility(View.VISIBLE);
                    menu_item.setVisibility(View.VISIBLE);
                    menu_item.startAnimation(open_menu);
                    btn_menu.setRotation(180);
                }

                break;

            case R.id.btn_change_password:
                ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
                changePasswordDialog.show(getSupportFragmentManager(), "Change password dialog");

                break;

            case R.id.btn_history:
                Intent history_intent = new Intent(MainActivity.this, DeletedTaskActivity.class);
                startActivity(history_intent);

                break;
        }
    }
}