package com.prueba.dto;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {

	int recordCount;
    T response;
    HttpStatus status;
    
	public ApiResponse() {
		super();
	}
	
	
	public ApiResponse(T response, HttpStatus status) {
		super();
		this.response = response;
		this.status = status;
	}


	public ApiResponse(int recordCount, T response) {
		super();
		this.recordCount = recordCount;
		this.response = response;
	}

	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public T getResponse() {
		return response;
	}
	public void setResponse(T response) {
		this.response = response;
	}
}
