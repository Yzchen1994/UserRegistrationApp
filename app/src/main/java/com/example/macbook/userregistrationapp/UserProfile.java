package com.example.macbook.userregistrationapp;


        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.TextView;

public class UserProfile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView textView = (TextView) findViewById(R.id.textViewUserName);

        Intent intent = getIntent();

        String username = intent.getStringExtra(ActivityLogin.USER_NAME);

        textView.setText("Welcome User "+username);
    }

}