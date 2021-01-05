package com.sapient.learning.client;

import feign.Param;
import feign.RequestLine;

public interface MultiplicationService {

	@RequestLine("GET /multiply/{number}")
	public int multiply(@Param("number") int number);
}