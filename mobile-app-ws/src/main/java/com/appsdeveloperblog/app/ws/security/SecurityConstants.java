package com.appsdeveloperblog.app.ws.security;

public class SecurityConstants {
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	//public static final long EXPIRATION_TIME = 60000; // 1 mins
	
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String TOKEN_SECRET = "g5haga645agah";
}
