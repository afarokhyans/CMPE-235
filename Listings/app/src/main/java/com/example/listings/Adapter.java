package com.example.listings;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listings.model.Listing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    Context mContext;
    List<Listing> mData;

    public Adapter(Context mContext, List<Listing> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        try {
            URL imgUrl = new URL(mData.get(position).getPictureUrl());
            Bitmap bmp = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());

            holder.listingImage.setImageBitmap(bmp);
            holder.tvListingName.setText(mData.get(position).getName());
            holder.tvListingPrice.setText(mData.get(position).getPrice() + " per night");
            holder.tvAccomedation.setText(mData.get(position).getAccommodates() + " guests");
            holder.ratingBar.setRating(mData.get(position).getReviewScoresRating());
            holder.tvNumberReviews.setText(mData.get(position).getNumberofReviews() + " reviews");
            holder.tvListingId.setText(mData.get(position).getListingId());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return  mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView listingImage;
        TextView tvListingName;
        TextView tvListingPrice;
        TextView tvAccomedation;
        RatingBar ratingBar;
        TextView tvNumberReviews;
        TextView tvListingId;

        public myViewHolder(View itemView) {
            super(itemView);
            listingImage = itemView.findViewById(R.id.imageListing);
            tvListingName = itemView.findViewById(R.id.tvListingName);
            tvListingPrice = itemView.findViewById(R.id.tvPrice);
            tvAccomedation = itemView.findViewById(R.id.tvGuestCount);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvNumberReviews = itemView.findViewById(R.id.tvNumberReviews);
            tvListingId = itemView.findViewById(R.id.tvListingId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, ListingDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", tvListingId.getText().toString());
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }
}