package com.example.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.listings.model.Listing;
import com.example.listings.service.UserClient;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListingDetailActivity extends AppCompatActivity {

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://cmpe-term-project.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    String listingId;

    UserClient userClient = retrofit.create(UserClient.class);
    private ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            listingId = bundle.getString("id");
        }

        mLoadingProgress = findViewById(R.id.pb_loading);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            new ListingDetailQueryTask().execute();
        }
        catch (Exception e) {
            Log.d("error", e.getMessage());
        }
    }

    public class  ListingDetailQueryTask extends AsyncTask<Call, Void, Listing> {

        @Override
        protected Listing doInBackground(Call... calls) {
            SharedPreferences prefs = getSharedPreferences("myListingsPrefs", MODE_PRIVATE);
            String token = prefs.getString("listingsAccessToken", "No name defined");


            Call<Listing> call = userClient.getListingDetail(listingId, "Bearer " + token);
            Response<Listing> response;
            try {
                response = call.execute();
                Listing listingDetail = response.body();
                return listingDetail;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Listing result) {
            ImageView listingImage = findViewById(R.id.imageListing);
            CircleImageView hostImage = findViewById(R.id.circleImageView);
            URL imgUrl, hostProfileUrl;
            try {
                imgUrl = new URL(result.getPictureUrl());
                Bitmap bmpListingImage = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
                listingImage.setImageBitmap(bmpListingImage);

                hostProfileUrl = new URL(result.getHostPictureUrl());
                Bitmap bmpHostImage = BitmapFactory.decodeStream(hostProfileUrl.openConnection().getInputStream());
                hostImage.setImageBitmap(bmpHostImage);

                TextView tvListingName = findViewById(R.id.tvListingName);
                tvListingName.setText(result.getName());

                TextView tvListingPrice = findViewById(R.id.tvListingPrice);
                tvListingPrice.setText(result.getPrice() + " per night");

                TextView tvAccommodates = findViewById(R.id.tvGuestCount);
                tvAccommodates.setText(result.getAccommodates() + " guests");

                TextView tvBedBath = findViewById(R.id.tvBedBath);
                tvBedBath.setText(result.getBedrooms() + " bedroom(s) - " + result.getBathrooms() + " bathroom(s)");

                RatingBar ratingBar = findViewById(R.id.ratingBar);
                ratingBar.setRating(result.getReviewScoresRating());
                ratingBar.setVisibility(View.VISIBLE);

                TextView tvReviewCount = findViewById(R.id.tvNumberReviews);
                tvReviewCount.setText(result.getNumberofReviews() + " reviews");

                TextView tvListingDescription = findViewById(R.id.tvListingDescription);
                tvListingDescription.setText(result.getDescription());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mLoadingProgress.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgress.setVisibility((View.VISIBLE));
        }
    }
}
