package com.example.json_api_r4;

import android.os.Bundle;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class PrayerTimes extends AppCompatActivity {

    String city;
    String country;
    TextView tvPrayerTime;
    String url;
    InterstitialAd myInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prayer_times);
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

        AdRequest request = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712",
                request, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Toast.makeText(PrayerTimes.this, loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        myInterstitialAd = interstitialAd;
                        myInterstitialAd.show(PrayerTimes.this);

                        myInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                                Toast.makeText(PrayerTimes.this, "Ad Clicked", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent();
                                Toast.makeText(PrayerTimes.this, "Ad Closed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });








        city = "Lahore";
        country = "Pakistan";
        tvPrayerTime = findViewById(R.id.tvTimes);


        url = "https://api.aladhan.com/v1/calendarByCity/2017/4?city="+city+"&country="+country+"&method=2";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String text = "";
                            JSONArray arr = response.getJSONArray("data");
                            for(int day = 1; day<=30; day++) {
                                JSONObject temp = arr.getJSONObject(day);
                                JSONObject obj = temp.getJSONObject("timings");
                                JSONObject date = temp.getJSONObject("date").getJSONObject("gregorian");
                                String weekDay = date.getJSONObject("weekday").getString("en").toString();

                                String[] names = new String[]{"Fajr", "Sunrise", "Dhuhr", "Asr", "Sunset", "Maghrib",
                                        "Isha", "Imsak", "Midnight"};
                                text += weekDay+" "+date.getString("date")+"\n";
                                for (int i = 0; i < names.length; i++) {
                                    text += names[i] + " : " + obj.getString(names[i]) + "\n";
                                }
                                text+="\n";
                                tvPrayerTime.setText(text);
                            }



                        }catch (JSONException e)
                        {
                            Toast.makeText(PrayerTimes.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PrayerTimes.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);


    }
}