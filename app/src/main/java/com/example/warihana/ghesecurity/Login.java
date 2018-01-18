package com.example.warihana.ghesecurity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button btnLogin;
    Button btnRegister;

    private static final String TAG = "LOGIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.txtemail);
        password = findViewById(R.id.txtpass);
        btnLogin = findViewById(R.id.btnlogin);
        btnRegister = findViewById(R.id.btnlog);

        SharedPreferences sharedpref = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String useremail = sharedpref.getString("status", "");
        if(!useremail.equals("")){
            Intent dashboard = new Intent(Login.this, Dashboard.class);
            startActivity(dashboard);
        }

        //when the register button is clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = "";
                userEmail = email.getText().toString();

                String userPass = "";
                userPass = password.getText().toString();

                String type="login";
                BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                backgroundTask.execute(type, userEmail, userPass);
            }

        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(Login.this, Register.class);
                startActivity(dashboard);


            }

        });

    }

}