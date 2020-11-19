package com.example.githubtrendingclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Pattern;

import com.example.githubtrendingclient.Models.Req.UserSigninReq;
import com.example.githubtrendingclient.Models.Req.UserSignupReq;
import com.example.githubtrendingclient.Models.Res.UserData;
import com.example.githubtrendingclient.Networks.APIUtils;
import com.example.githubtrendingclient.Networks.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText etFullname;
    private EditText etMail;
    private EditText etPassword;
    private Button btnSignup;
    String fullname, email, password;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private boolean validateEmailAddress(EditText email) {
        String emailInput = email.getText().toString();

        if (!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            return true;
        } else {
            Toast.makeText(SignupActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validateFullname(EditText fullname) {
        String usernameInput = fullname.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Field full name can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (usernameInput.length() > 10) {
            Toast.makeText(this, "Full  too long", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword(EditText password) {
        String passwordInput = password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Field password can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            Toast.makeText(SignupActivity.this, "Password too weak!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etFullname = findViewById(R.id.etFullnameSignup);
        etMail = findViewById(R.id.etEmailSignup);
        etPassword = findViewById(R.id.etPasswordSignup);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = etFullname.getText().toString();
                email = etMail.getText().toString();
                password = etPassword.getText().toString();

                UserSignupReq userSignupReq = new UserSignupReq(fullname, email, password);

                if (validateEmailAddress(etMail) == true && validateFullname(etFullname) == true && validatePassword(etPassword) == true ) {
                    DataClient dataClient = APIUtils.getData();
                    Call<UserData> callback = dataClient.signup(userSignupReq);
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

                                Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, ContentActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserData> call, Throwable t) {
                            Toast.makeText(SignupActivity.this, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    validateEmailAddress(etMail);
                    validateFullname(etFullname);
                    validatePassword(etPassword);
                }
            }
        });

    }
}