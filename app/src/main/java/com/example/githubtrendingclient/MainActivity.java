package com.example.githubtrendingclient;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubtrendingclient.Models.Req.UserSigninReq;
import com.example.githubtrendingclient.Models.Res.UserData;
import com.example.githubtrendingclient.Models.Res.UserProfileData;
import com.example.githubtrendingclient.Networks.APIUtils;
import com.example.githubtrendingclient.Networks.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private TextView tvSignup;
    private Button btnSignin;
    private EditText etEmail;
    private EditText etPassword;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignin = findViewById(R.id.btnSignin);
        tvSignup = findViewById(R.id.tvSignup);
        etEmail = findViewById(R.id.etEmailSignin);
        etPassword = findViewById(R.id.etPasswordSignin);

        SharedPreferences pref = getSharedPreferences("token", Context.MODE_PRIVATE);
        if (pref.contains("token")) {
            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
            startActivity(intent);
        }

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                UserSigninReq userSigninReq = new UserSigninReq(email, password);

                if (email.length() > 0 && password.length() > 0) {
                    DataClient dataClient = APIUtils.getData();
                    Call<UserData> callback = dataClient.signin(userSigninReq);
                    callback.enqueue(new Callback<UserData>() {
                        @Override
                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                            if (response.isSuccessful()) {
                                SharedPreferences pref = getSharedPreferences("token", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.apply();
                                editor.putString("token", response.body().getData().getToken());
                                editor.commit();

                                Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserData> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
//                                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
//                                startActivity(intent);
            }
        });



        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}