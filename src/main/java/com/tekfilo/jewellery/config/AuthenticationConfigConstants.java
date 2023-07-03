package com.tekfilo.jewellery.config;

public class AuthenticationConfigConstants {
    public static final String SECRET = "tekfilo";
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
