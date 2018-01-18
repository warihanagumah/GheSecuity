package com.example.warihana.ghesecurity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText name, email, phone, age, password;
    Button btnLogin, btnRegister;
    private static final String TAG = "REGISTER_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.txtemail);
        password = findViewById(R.id.txtpass);
        phone = findViewById(R.id.txtphone);
        age = findViewById(R.id.txtage);
        name = findViewById(R.id.txtname);
        btnRegister = findViewById(R.id.btnregister);
        btnLogin = findViewById(R.id.btnLogin);

        SharedPreferences sharedpref = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        String useremail = sharedpref.getString("status", "");

        if(!useremail.equals("")){
            Intent dashboard = new Intent(Register.this, Dashboard.class);
             startActivity(dashboard);
        }

        //when the register button is clicked
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail = "";
                userEmail = email.getText().toString();

                String userPass = "";
                userPass = password.getText().toString();

                String userPhone = "";
                userPhone = phone.getText().toString();

                String userAge = "";
                userAge = age.getText().toString();

                String userName = "";
                userName = name.getText().toString();

                String type="register";
                BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                backgroundTask.execute(type, userEmail, userPass,userPhone, userAge,userName);
            }

        });

        //when the login button is clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoLogin = new Intent(Register.this, Login.class);
                startActivity(gotoLogin);
            }
        });

    }


}