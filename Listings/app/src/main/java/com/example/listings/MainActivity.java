package com.example.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.listings.model.Listing;
import com.example.listings.service.UserClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ProgressBar mLoadingProgress;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://cmpe-term-project.azurewebsites.net/api/listing/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgress = findViewById(R.id.pb_loading);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            new ListingsQueryTask().execute();
        }
        catch (Exception e) {
            Log.d("error", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPreferences prefs = getSharedPreferences("myListingsPrefs", MODE_PRIVATE);
                prefs.edit().remove("listingsAccessToken").commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                // Clear task so clicking back button will not take the user to listing page
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    public class  ListingsQueryTask extends AsyncTask<Call, Void, List<Listing>> {

        @Override
        protected List<Listing> doInBackground(Call... calls) {
            SharedPreferences prefs = getSharedPreferences("myListingsPrefs", MODE_PRIVATE);
            String token = prefs.getString("listingsAccessToken", "No name defined");

            // For demo, hardcoded to get 50 entries from record 300 to 350
            Call<List<Listing>> call = userClient.getListings("300", "350", "Bearer " + token);
            Response<List<Listing>> response;
            try {
                response = call.execute();
                List<Listing> listings = new ArrayList<>(response.body());
                return listings;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Listing> result) {
            RecyclerView recyclerView = findViewById(R.id.rv_listings);
            Adapter adapter = new Adapter(MainActivity.this, result);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            mLoadingProgress.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility((View.VISIBLE));
        }
    }
}
