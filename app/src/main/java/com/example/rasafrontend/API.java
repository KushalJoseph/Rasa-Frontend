package com.example.rasafrontend;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {

    @POST("/")
    Call<ResponseObject> getData(@Body RequestObject requestObject);
}
