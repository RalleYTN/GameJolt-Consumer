package de.ralleytn.api.gamejolt.internal;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class Utils {

	private Utils() {}
	
	public static final long[] toLongArray(Collection<Long> collection) {
		
		int index = 0;
		long[] array = new long[collection.size()];
		
		for(Long value : collection) {
			
			array[index++] = value;
		}
		
		return array;
	}
	
	public static final String createSignature(String base, String privateKey) {
		
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(base);
		signatureBuilder.append(privateKey);
		return Utils.hashSHA1(signatureBuilder.toString());
	}
	
	public static final String hashSHA1(String message) {

		try {
			
			return Utils.toHexString(MessageDigest.getInstance("SHA-1").digest(message.getBytes("UTF-8")));
			
		} catch(UnsupportedEncodingException | NoSuchAlgorithmException exception) {
			
			// SHOULD NEVER HAPPEN
			throw new RuntimeException(exception);
		}
	}
	
	public static final String getQueryString(Map<String, ?> params) {

		try {
			
			StringBuilder result = new StringBuilder().append('?');
			boolean first = true;
				
			for(Map.Entry<String, ?> entry : params.entrySet()) {
					
				if(first) first = false; else result.append('&');
				result.append(URLEncoder.encode(entry.getKey().toString(), "UTF-8")).append('=');
				boolean firstElement = true;
				Object value = entry.getValue();
					
				if(value instanceof List) {
						
					for(Object element : (List<?>)value) {
							
						if(firstElement) firstElement = false; else result.append(',');
						result.append(URLEncoder.encode(element.toString(), "UTF-8"));
					}
						
				} else if(value != null && value.getClass().isArray()) {
						
					for(int index = 0; index < Array.getLength(entry.getValue()); index++) {
							
						if(firstElement) firstElement = false; else result.append(',');
						result.append(URLEncoder.encode(Array.get(value, index).toString(), "UTF-8"));
					}
						
				} else {

					result.append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
				}
			}
				
			return result.toString();
			
		} catch(UnsupportedEncodingException exception) {
			
			// SHOULD NEVER HAPPEN!
			throw new RuntimeException(exception);
		}
	}
	
	public static final String toHexString(byte[] binary) {
		
		StringBuilder hexBuilder = new StringBuilder();
		
		for(byte b : binary) {
			
			hexBuilder.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
		}
		
		return hexBuilder.toString();
	}
}
