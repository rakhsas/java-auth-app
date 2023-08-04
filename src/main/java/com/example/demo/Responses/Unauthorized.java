package com.example.demo.Responses;

public class Unauthorized {
	private String message;

	public Unauthorized ( String message ) {
		this.message = message;
	}

	public String getUnauthorized() {
		return message;
	}
	public void setUnauthorized( String message ) {
		this.message = message;
	}
}
