package com.example.monica.nextgentest.services;

import com.example.monica.nextgentest.models.Github;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface GithubService {
    String SERVICE_ENDPOINT = "https://api.github.com";

    @GET("/users/{login}")
    Observable<Github> getUser(@Path("login") String login);
}
