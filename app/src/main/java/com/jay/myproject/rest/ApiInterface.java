package com.jay.myproject.rest;

import com.jay.myproject.model.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("/api/users/1")
    Call<Model> getDetails();

}
