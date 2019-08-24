package com.example.listings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listings.model.Login;
import com.example.listings.model.User;
import com.example.listings.service.UserClient;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://cmpe-term-project.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }});
    }

    private static String token;

    private void login() {
        String username = mUserName.getText().toString();
        String password = mPassword.getText().toString();

        if(username.isEmpty() || password.isEmpty() ) {
            Toast.makeText(LoginActivity.this, "Username or Password cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }

        Login login = new Login(username,password);
        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    token = response.body().getToken();

                    // Store JWT token in shared preferences
                    SharedPreferences.Editor editor = getSharedPreferences("myListingsPrefs", MODE_PRIVATE).edit();
                    editor.putString("listingsAccessToken", token);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Login Failed :(", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "error :(", Toast.LENGTH_LONG).show();
            }
        });
    }

}
