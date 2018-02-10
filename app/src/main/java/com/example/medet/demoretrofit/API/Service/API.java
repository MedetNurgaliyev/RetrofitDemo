package com.example.medet.demoretrofit.API.Service;

import com.example.medet.demoretrofit.API.model.GitHub;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by Medet on 10.02.2018.
 */

public interface API {

    @GET("/")
    Call<GitHub> getGitHubData(@Header("Authorization") String basicAuth);
}
