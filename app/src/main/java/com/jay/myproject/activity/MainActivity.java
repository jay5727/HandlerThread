package com.jay.myproject.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jay.myproject.R;
import com.jay.myproject.model.Data;
import com.jay.myproject.model.Model;
import com.jay.myproject.rest.ApiClient;
import com.jay.myproject.rest.ApiInterface;
import com.jay.myproject.util.InternetStatus;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView textViewProgress;
    private int progress = 0;
    private final int progressBarMax = 100;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        textViewProgress = findViewById(R.id.textViewProgress);
        progressBar.setMax(progressBarMax);

        final Thread progressBarThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (progress <= progressBarMax) {
                        progressBar.setProgress(progress);
                        textViewProgress.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                textViewProgress.setText(String.valueOf(progress));
                            }
                        }, 2);
                        sleep(TimeUnit.SECONDS.toMillis(2));
                        progress += 10;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        progressBarThread.start();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if (InternetStatus.checkConnection(MainActivity.this)) {
                    if (progress <= progressBarMax) {
                        doNetworkRequest();
                        handler.postDelayed(this, TimeUnit.SECONDS.toMillis(5));
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        };
        runnable.run();
    }

    private void doNetworkRequest() {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Model> call = apiService.getDetails();

        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        Data data = response.body().getData();
                        if (data.getId() != null && data.getFirstName() != null && data.getLastName() != null) {
                            int id = data.getId();
                            String nameF = data.getFirstName();
                            String nameL = data.getLastName();
                            Toast.makeText(MainActivity.this, "ID: " + id + "\n" +
                                    "First Name: " + nameF + "\n" +
                                    "Last Name: " + nameL, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Do not display unnecessary toast if app is not used ...
        if (handler != null)
            handler.removeCallbacks(runnable);
    }
}
