package com.kchandrakant.learning.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AdditionService {

	@GET("/add/{number}")
	public Call<Integer> add(@Path("number") int number);
}