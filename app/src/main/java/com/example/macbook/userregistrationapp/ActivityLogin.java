package com.example.macbook.userregistrationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener{

    public static final String USER_NAME = "USER_NAME";

    public static final String PASSWORD = "PASSWORD";

    private static final String LOGIN_URL = "http://myphpprojandroid1221.esy.es/UserRegistration/login.php";

    private EditText editTextUserName;
    private EditText editTextPassword;

    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUserName = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

        buttonLogin = (Button) findViewById(R.id.buttonUserLogin);
        buttonRegister=(Button) findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }


    private void login(){
        String username = editTextUserName.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim();
        boolean hasErr=false;
        if(!isValidUsername(username)){
            hasErr=true;
            editTextUserName.setError("Invalid username format.");
            Toast.makeText(getApplicationContext(), "Invalid username format.", Toast.LENGTH_SHORT).show();
        }

        if(!isValidPassword(password)){
            hasErr=true;
            editTextPassword.setError("Invalid password format.");
            Toast.makeText(getApplicationContext(), "Invalid password format.", Toast.LENGTH_SHORT).show();
        }

        if(!hasErr) {
            userLogin(username, password);
        }
    }

    public boolean isValidUsername(String username) {
        String ePattern = "^[a-z0-9_-]{3,16}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(username);
        return m.matches();
    }

    public boolean isValidPassword(String password) {
        String ePattern = "^[A-Za-z0-9_!@#$%^&*-]{6,18}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(password);
        return m.matches();
    }

    private void userLogin(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityLogin.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Intent intent = new Intent(ActivityLogin.this,UserProfile.class);
                    intent.putExtra(USER_NAME,username);
                    startActivity(intent);
                }else{
                    Toast.makeText(ActivityLogin.this,s,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username,password);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            login();
        }
        if(v==buttonRegister){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}