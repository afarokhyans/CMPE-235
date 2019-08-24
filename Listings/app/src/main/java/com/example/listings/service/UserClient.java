package com.example.listings.service;

import com.example.listings.model.Listing;
import com.example.listings.model.Login;
import com.example.listings.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserClient {
    @POST("login")
    Call<User> login(@Body Login login);

    @GET("paged")
    Call<List<Listing>> getListings(
            @Query("from") String from,
            @Query("count") String page,
            @Header("Authorization")  String authToken);

    @GET("listing/{id}")
    Call<Listing> getListingDetail(
            @Path(value = "id") String id,
            @Header("Authorization")  String authToken);
}
