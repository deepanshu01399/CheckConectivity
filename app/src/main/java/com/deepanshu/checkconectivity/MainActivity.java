package com.deepanshu.checkconectivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.deepanshu.checkconectivity.ConnectivityReceiver;
import com.google.android.material.snackbar.Snackbar;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    private Button btnCheck;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    GifImageView NoInternetGif;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setSupportActionBar(toolbar);

        btnCheck = (Button) findViewById(R.id.btn_check);
        NoInternetGif = findViewById(R.id.NoInternetGif);
        linearLayout = findViewById(R.id.linearlayout);

        // Manually checking internet connection
        checkConnection();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manually checking internet connection
                checkConnection();
            }
        });
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
            NoInternetGif.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            ColoredSnackbar.confirm(Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)).show();

        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            ColoredSnackbar.alert(Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)).show();
            alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle("Internet Alert")
                    .setMessage("Internet connection is lost ! please check connection..")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            linearLayout.setVisibility(View.GONE);
                            NoInternetGif.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            checkConnection();

                        }

                    });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            buttonPositive.setTextColor(ContextCompat.getColor(this, R.color.green));
            buttonPositive.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            Button buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            buttonNegative.setTextColor(ContextCompat.getColor(this, R.color.red));
            buttonNegative.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }
       /* Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();*/
    }
    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        Myapplication.getInstance().setConnectivityListener(this);
    }
    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
    public void lightmode(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
    public void nighmode(View view) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
