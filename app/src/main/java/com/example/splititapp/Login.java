package com.example.splititapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button loginButton, createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextTextEmailAddress3);
        password = findViewById(R.id.editTextTextPassword5);
        loginButton = findViewById(R.id.logInbtn);
        createAccountButton = findViewById(R.id.createbtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, com.example.splititapp.Register.class);
                startActivity(intent);
            }
        });
    }

    public void loginUser() {
        String url = "http://10.0.2.2/split_it/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);

                        if (obj.getString("message").contains("Login Successful")) {

                            // 3. Get the ID and Save it in SharedPreferences
                            String userId = obj.getString("id");
                            SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("user_id", userId);
                            editor.apply();

                            Intent intent = new Intent(Login.this, com.example.splititapp.MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(Login.this, "Error connecting to XAMPP", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}