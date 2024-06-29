package com.example.json_api_r4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView tvRecords;
    String url;
    String text;
    Button btnPrayerTime;
    AdView adview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });


        tvRecords = findViewById(R.id.tvRecord);
        btnPrayerTime = findViewById(R.id.btnPrayerTimes);
        adview = findViewById(R.id.adview);
        btnPrayerTime.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, PrayerTimes.class));
        });

        AdRequest request = new AdRequest.Builder().build();
        adview.loadAd(request);

        adview.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Toast.makeText(MainActivity.this, "Ad Clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toast.makeText(MainActivity.this, "Ad Closed", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);

        text = "";
        for(int i=1; i<=50; i++)
        {
            url = "https://jsonplaceholder.typicode.com/todos/"+i;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int userID = response.getInt("userId");
                                int id = response.getInt("id");
                                String title = response.getString("title");
                                boolean completed = response.getBoolean("completed");

                                text += "ID : "+id+"\nTITLE : "+title+"\n\n";
                                tvRecords.setText(text);

                            }catch (JSONException e)
                            {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            queue.add(jsonObjectRequest);

        }








    }
}