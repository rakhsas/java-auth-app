package com.example.demo.Responses;

public class JwtToken {

	private String Token;

	public JwtToken (String Token)
	{
		setToken(Token);
	}

	public String getToken()
	{
		return Token;
	}

	public void setToken( String Token )
	{
		this.Token = Token;
	}
}
