package com.example.githubrepolister.network;

import com.example.githubrepolister.network.model.RepoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebService {

    @GET("users/{user}/repos")
    Call<List<RepoResponse>> getUserRepos (@Path("user") String username);
}
