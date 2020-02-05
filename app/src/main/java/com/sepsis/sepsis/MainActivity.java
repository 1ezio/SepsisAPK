package com.sepsis.sepsis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button signup, login, about;
    SessionManager session;
    private String username;

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.theme_welcome);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
            getWindow().setStatusBarColor(getResources().getColor(R.color.blueAccent));
        }
        setContentView(R.layout.layout_welcome);


        signup = findViewById(R.id.welcome_signup);
        login = findViewById(R.id.welcome_login);
        about=findViewById(R.id.about);

        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn())
        {
            Intent i = new Intent(MainActivity.this, dashboard.class);
            startActivity(i);
            finish();
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(MainActivity.this, signup.class);
                startActivity(activity);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(MainActivity.this, staff_login.class);
                startActivity(activity);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, about.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}
