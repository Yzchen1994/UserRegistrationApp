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

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.net.URLEncoder;
        import java.util.HashMap;
        import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private Button buttonRegister;
    private Button buttonLogin;

    private static final String REGISTER_URL = "http://myphpprojandroid1221.esy.es/UserRegistration/register.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();



    }

    private void initFields(){
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonRegister.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }


        if(v==buttonLogin){
            startActivity(new Intent(this,ActivityLogin.class));
        }
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim().toLowerCase();
        String username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim().toLowerCase();

        boolean hasErr=false;
        //Validates the email
        if(!isValidEmailAddress(email)) {
            hasErr=true;
            editTextEmail.setError("Invalid email");
            Toast.makeText(getApplicationContext(), "Invalid email address.", Toast.LENGTH_LONG).show();
        }

        //Validates the name
        if(name.length()==0){
            hasErr=true;
            editTextName.setError("Please enter your name.");
            Toast.makeText(getApplicationContext(), "Please enter your name.", Toast.LENGTH_LONG).show();
        }

        //Validates the password
        if(!isValidPassword(password)){
            hasErr=true;
            editTextPassword.setError("Password must be 6-18 characters, contains only 0-9 a-z A-Z _!@#$%^&*-");
            Toast.makeText(getApplicationContext(), "Password must be 6-18 characters, contains only 0-9 a-z A-Z _!@#$%^&*-", Toast.LENGTH_LONG).show();
        }

        //Validates the username
        if(!isValidUsername(username)){
            hasErr=true;
            editTextUsername.setError("Username must begin with a letter, has 3-16 characters, contains only 0-9, a-z, and '-' character.");
            Toast.makeText(getApplicationContext(), "Username must begin with a letter, has 3-16 characters, contains only 0-9, a-z, and '-' character.", Toast.LENGTH_LONG).show();
        }

        if(!hasErr){
            register(name, username, password, email);
        }


    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
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

    private void register(String name, String username, String password, String email) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("username",params[1]);
                data.put("password",params[2]);
                data.put("email",params[3]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,username,password,email);
    }
}