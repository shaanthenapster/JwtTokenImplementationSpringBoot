package com.rme.jwt.security;



public class SecurityConstants {
	
	public static String SECRET = "sanu001#";
	public static String TOKEN_PREFIX = "PREFIX";
	public static String HEADER_STRING = "HEADER";
	public long  EXPIRATION_TIME = 846_000_000L;
	public static String getSecret() {
		return SECRET;
	}
	public static String getTokenPrefix() {
		return TOKEN_PREFIX;
	}
	public static String getHeaderString() {
		return HEADER_STRING;
	}
	public long getEXPIRATION_TIME() {
		return EXPIRATION_TIME;
	}
}
