/*
 * MIT License
 * 
 * Copyright (c) 2017 Ralph Niemitz
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.ralleytn.api.gamejolt.internal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.ralleytn.api.gamejolt.GameJolt;
import de.ralleytn.api.gamejolt.GameJoltException;
import de.ralleytn.simple.json.JSONArray;
import de.ralleytn.simple.json.JSONObject;
import de.ralleytn.simple.json.JSONParseException;
import de.ralleytn.simple.json.JSONParser;

/**
 * 
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Util {

	private Util() {}
	
	/**
	 * 
	 * @param array
	 * @param type
	 * @param consumer
	 * @return
	 * @since 1.0.0
	 */
	public static final <T extends GameJoltObject>List<T> toList(JSONArray array, Class<T> type, GameJolt consumer) {
		
		List<T> list = new ArrayList<>();
		
		for(Object element : array) {
			
			try {
				
				Constructor<T> constructor = type.getDeclaredConstructor(GameJolt.class, JSONObject.class);
				constructor.setAccessible(true);
				list.add(constructor.newInstance(consumer, (JSONObject)element));
				constructor.setAccessible(false);
			
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
				
				// SHOULD NEVER HAPPEN!
				throw new RuntimeException(exception);
			}
		}
		
		return list;
	}
	
	/**
	 * 
	 * @param requestURL
	 * @param requestMethod
	 * @param doOutput
	 * @return
	 * @throws IOException
	 * @since 1.0.0
	 */
	public static final HttpURLConnection createConnection(String requestURL, String requestMethod, boolean doOutput) throws IOException {

		HttpURLConnection connection = (HttpURLConnection)new URL(requestURL).openConnection();
		connection.setRequestMethod(requestMethod);
		connection.setAllowUserInteraction(false);
		connection.setConnectTimeout(5000); // 5 seconds
		connection.setReadTimeout(300000); // 5 min
		connection.setDefaultUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(doOutput);
		connection.setInstanceFollowRedirects(false);
		connection.setUseCaches(false);
		
		return connection;
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 * @since 1.0.0
	 */
	public static final String read(InputStream inputStream) throws IOException {
		
		try(BufferedReader reader = Util.createReader(inputStream)) {
			
			StringBuilder contentBuilder = new StringBuilder();
			String line = null;
			
			while((line = reader.readLine()) != null) {
				
				contentBuilder.append(line);
				contentBuilder.append('\n');
			}
			
			return contentBuilder.toString();
		}
	}
	
	/**
	 * 
	 * @param service
	 * @param connection
	 * @return
	 * @throws GameJoltException
	 * @throws IOException
	 * @throws JSONParseException
	 * @since 1.0.0
	 */
	public static final JSONObject finishRequest(GameJolt service, HttpURLConnection connection) throws GameJoltException, IOException, JSONParseException {
		
		int status = connection.getResponseCode();
		
		if(status == HttpURLConnection.HTTP_OK) {
			
			try(BufferedReader reader = Util.createReader(connection.getInputStream())) {
				
				Object parsedObject = new JSONParser().parse(reader);
				
				if(parsedObject instanceof JSONObject) {
					
					return ((JSONObject)parsedObject).getObject("response");
					
				} else {
					
					throw new GameJoltException(service, "A JSON object was expected, not an array!");
				}
			}
			
		} else {
			
			throw new GameJoltException(service, String.format("%d %s: %s", status, connection.getResponseMessage(), Util.read(connection.getErrorStream())));
		}
	}
	
	/**
	 * 
	 * @param outputStream
	 * @param content
	 * @throws IOException
	 * @since 1.0.0
	 */
	public static final void write(OutputStream outputStream, String content) throws IOException {
		
		try(BufferedWriter writer = Util.createWriter(outputStream)) {
			
			writer.write(content);
			writer.flush();
		}
	}
	
	/**
	 * 
	 * @param inputStream
	 * @return
	 * @since 1.0.0
	 */
	public static final BufferedReader createReader(InputStream inputStream) {
		
		return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}
	
	/**
	 * 
	 * @param outputStream
	 * @return
	 * @since 1.0.0
	 */
	public static final BufferedWriter createWriter(OutputStream outputStream) {
		
		return new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
	}
	
	/**
	 * 
	 * @param collection
	 * @return
	 * @since 1.0.0
	 */
	public static final long[] toLongArray(Collection<Long> collection) {
		
		int index = 0;
		long[] array = new long[collection.size()];
		
		for(Long value : collection) {
			
			array[index++] = value;
		}
		
		return array;
	}
	
	/**
	 * 
	 * @param base
	 * @param privateKey
	 * @return
	 * @since 1.0.0
	 */
	public static final String createSignature(String base, String privateKey) {
		
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(base);
		signatureBuilder.append(privateKey);
		return Util.hashSHA1(signatureBuilder.toString());
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 * @since 1.0.0
	 */
	public static final String hashSHA1(String message) {

		try {
			
			return Util.toHexString(MessageDigest.getInstance("SHA-1").digest(message.getBytes("UTF-8")));
			
		} catch(UnsupportedEncodingException | NoSuchAlgorithmException exception) {
			
			// SHOULD NEVER HAPPEN
			throw new RuntimeException(exception);
		}
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 * @since 1.0.0
	 */
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
	
	/**
	 * 
	 * @param binary
	 * @return
	 * @since 1.0.0
	 */
	public static final String toHexString(byte[] binary) {
		
		StringBuilder hexBuilder = new StringBuilder();
		
		for(byte b : binary) {
			
			hexBuilder.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
		}
		
		return hexBuilder.toString();
	}
}
