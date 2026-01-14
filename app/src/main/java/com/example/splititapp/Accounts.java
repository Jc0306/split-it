package com.example.splititapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Accounts extends AppCompatActivity {

    private EditText etName, etEmail, etBirthdate, etPassword;
    private TextView tvHeaderName, tvHeaderEmail;
    private ImageButton btnEditToggle;
    private Button btnSave;
    private boolean isEditMode = false;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        // 1. Initialize SharedPreferences
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = pref.getString("user_id", null);

        if (userId == null) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Initialize Views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etBirthdate = findViewById(R.id.etBirthdate);
        etPassword = findViewById(R.id.etPassword);
        tvHeaderName = findViewById(R.id.tvHeaderName);
        tvHeaderEmail = findViewById(R.id.tvHeaderEmail);
        btnEditToggle = findViewById(R.id.btnEditToggle);
        btnSave = findViewById(R.id.btnSave);
        Button btnLogout = findViewById(R.id.logoutbtn);

        // 3. Logout Logic (Correctly placed in onCreate)
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(Accounts.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        fetchUserData();

        btnEditToggle.setOnClickListener(v -> {
            isEditMode = !isEditMode;
            toggleFields(isEditMode);
        });

        btnSave.setOnClickListener(v -> {
            updateUserData();
            isEditMode = false;
            toggleFields(false);
        });
    }

    private void toggleFields(boolean enable) {
        etName.setEnabled(enable);
        etEmail.setEnabled(false); // Locked as requested
        etBirthdate.setEnabled(enable);
        etPassword.setEnabled(enable);
        btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
        btnEditToggle.setImageResource(enable ? android.R.drawable.ic_menu_close_clear_cancel : android.R.drawable.ic_menu_edit);
    }

    private void fetchUserData() {
        String url = "http://10.0.2.2/split_it/get_profile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.has("error")) {
                            etName.setText(obj.getString("fullname"));
                            etEmail.setText(obj.getString("email"));
                            etBirthdate.setText(obj.getString("birthdate"));
                            tvHeaderName.setText(obj.getString("fullname"));
                            tvHeaderEmail.setText(obj.getString("email"));
                            etPassword.setText(obj.getString("password"));

                            tvHeaderName.setText(obj.getString("fullname"));
                            tvHeaderEmail.setText(obj.getString("email"));
                        }
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", response);
                    }
                }, error -> Log.e("API_ERROR", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void updateUserData() {
        String url = "http://10.0.2.2/split_it/update_profile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        Toast.makeText(this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        tvHeaderName.setText(etName.getText().toString());
                        tvHeaderEmail.setText(etEmail.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("fullname", etName.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("birthdate", etBirthdate.getText().toString());

                // ADD THIS LINE:
                params.put("password", etPassword.getText().toString());

                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}