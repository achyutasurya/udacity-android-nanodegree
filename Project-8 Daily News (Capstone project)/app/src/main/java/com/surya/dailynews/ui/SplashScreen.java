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
import android.view.WindowManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.surya.dailynews.R;

public class SplashScreen extends AppCompatActivity {


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth = FirebaseAuth.getInstance();
        checkNow();

    }

    void checkNow() {
        if (isNetworkAvailable()) {
            if (auth.getCurrentUser() != null) {
                login();
            } else {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.a9))
                    .setMessage(getString(R.string.a10))
                    .setPositiveButton(getString(R.string.a11), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkNow();
                        }
                    })
                    .setNegativeButton(getString(R.string.a12), null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    void login() {

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_main), Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString(getString(R.string.shared_mobile), "0");
        String password = sharedPreferences.getString(getString(R.string.shared_password), "0");
        assert password != null;
        assert mobile != null;
        if(!(mobile.equals("0") || password.equals("0"))) {
            auth.signInWithEmailAndPassword(mobile, password)
                    .addOnCompleteListener(SplashScreen.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                auth.signOut();
                                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                                finish();
                            } else {
                                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }else{
            auth.signOut();
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
