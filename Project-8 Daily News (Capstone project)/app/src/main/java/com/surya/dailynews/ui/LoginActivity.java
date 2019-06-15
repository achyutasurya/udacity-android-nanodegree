package com.surya.dailynews.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.surya.dailynews.R;

public class LoginActivity extends AppCompatActivity {

    private EditText inputMobile, inputPassword;
    private Button btnSign;
    private TextView registerTV, signTV;
    private FirebaseAuth auth;
    boolean isLogin = true;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        auth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        btnSign = findViewById(R.id.button_screen);
        inputMobile = findViewById(R.id.mobile_number_e);
        inputPassword = findViewById(R.id.password_et);
        registerTV = findViewById(R.id.sign_text_button);
        signTV = findViewById(R.id.sign_text);

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin) {
                    registerTV.setText(getString(R.string.reg));
                    signTV.setText(getString(R.string.login));
                    btnSign.setText(getString(R.string.login));
                    isLogin = true;

                } else {

                    registerTV.setText(getString(R.string.log));
                    signTV.setText(getString(R.string.signup));
                    btnSign.setText(getString(R.string.signup));
                    isLogin = false;
                }
            }
        });


        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = inputMobile.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(mobile)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.a6), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(),getString(R.string.a7) , Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(),getString(R.string.a8) , Toast.LENGTH_SHORT).show();
                    return;
                }

                checkNow(mobile, password);
            }
        });


    }

    void checkNow(final String mobile, final String password) {
        if (isNetworkAvailable()) {
            if (isLogin) {
                login(mobile, password);
            } else {
                signUp(mobile, password);
            }
        } else {

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.a9))
                    .setMessage(getString(R.string.a10))
                    .setPositiveButton(getString(R.string.a11), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkNow(mobile, password);
                        }
                    })
                    .setNegativeButton(getString(R.string.a12), null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void saveUserDetails(String mobile, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_main), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.shared_mobile), mobile);
        editor.putString(getString(R.string.shared_password), password);
        editor.apply();
    }

    void login(final String mobie, final String password) {
        auth.signInWithEmailAndPassword(mobie, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.a13));
                            } else {
                                Toast.makeText(LoginActivity.this,getString(R.string.a14) , Toast.LENGTH_LONG).show();
                            }
                        } else {
                            saveUserDetails(mobie, password);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    void signUp(final String mobile, final String password) {
        auth.createUserWithEmailAndPassword(mobile, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(LoginActivity.this, getString(R.string.a15) + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, getString(R.string.a16) + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            saveUserDetails(mobile, password);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
