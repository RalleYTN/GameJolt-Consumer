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
package de.ralleytn.api.gamejolt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ralleytn.simple.json.JSONArray;
import de.ralleytn.simple.json.JSONObject;
import de.ralleytn.simple.json.JSONParseException;

/**
 * Represents a game's data storage.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class GameJoltDataStorage {

	private final GameJolt gj;
	private final boolean global;
	
	GameJoltDataStorage(GameJolt gj, boolean global) {
		
		this.gj = gj;
		this.global = global;
	}
	
	// ----------------------------------------------------------------------------------------------
	
	// ==== POST /data-store/update
	
	/**
	 * Updates an entry.
	 * @param key the entry key
	 * @param value the value
	 * @param operation the operation
	 * @return the new value of the entry
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final String update(String key, String value, Operation operation) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("key", key);
		params.put("operation", operation.toString().toLowerCase());
		
		if(!this.global) {
			
			params.put("username", this.gj.getUsername());
			params.put("user_token", this.gj.getUserToken());
		}
		
		Map<String, Object> postParams = new HashMap<>();
		postParams.put("value", value);
		
		JSONObject response = this.gj.post("/data-store/update", params, postParams);
		this.gj.checkStatus(response);
		return response.getString("data");
	}
	
	// ==== POST /data-store/set
	
	/**
	 * Replaces the data of an old entry or creates a new entry.
	 * @param key the entry key
	 * @param data the data
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final void set(String key, String data) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("key", key);
		
		if(!this.global) {
			
			params.put("username", this.gj.getUsername());
			params.put("user_token", this.gj.getUserToken());
		}
		
		Map<String, Object> postParams = new HashMap<>();
		postParams.put("data", data);
		
		JSONObject response = this.gj.post("/data-store/set", params, postParams);
		this.gj.checkStatus(response);
	}
	
	// ==== GET /data-store/remove
	
	/**
	 * Removes an entry.
	 * @param key the entry key
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final void remove(String key) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("key", key);
		
		if(!this.global) {
			
			params.put("username", this.gj.getUsername());
			params.put("user_token", this.gj.getUserToken());
		}
		
		JSONObject response = this.gj.get("/data-store/remove", params);
		this.gj.checkStatus(response);
	}
	
	// ==== GET /data-store
	
	/**
	 * @param key the entry key
	 * @return the data of an entry
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final String get(String key) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("key", key);
		
		if(!this.global) {
			
			params.put("username", this.gj.getUsername());
			params.put("user_token", this.gj.getUserToken());
		}
		
		JSONObject response = this.gj.get("/data-store", params);
		this.gj.checkStatus(response);
		return response.getString("data");
	}
	
	// ==== GET /data-store/get-keys
	
	/**
	 * @return a list of entry keys
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final List<String> getKeys() throws IOException, GameJoltException, JSONParseException {
		
		return this.getKeys(null);
	}
	
	/**
	 * @param pattern the pattern
	 * @return a list of entry keys based on the given pattern
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final List<String> getKeys(String pattern) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		
		if(!this.global) {
			
			params.put("username", this.gj.getUsername());
			params.put("user_token", this.gj.getUserToken());
		}
		
		if(pattern != null) {
			
			params.put("pattern", pattern);
		}
		
		JSONObject response = this.gj.get("/data-store/get-keys", params);
		this.gj.checkStatus(response);
		JSONArray array = response.getArray("keys");
		List<String> keys = new ArrayList<>();
		
		for(Object element : array) {
			
			keys.add(((JSONObject)element).getString("key"));
		}
		
		return keys;
	}
	
	// ----------------------------------------------------------------------------------------------
	
	/**
	 * @return the service consumer this data storage belongs to
	 * @since 1.0.0
	 */
	public final GameJolt getServiceConsumer() {
		
		return this.gj;
	}
	
	/**
	 * @return {@code true} if this is the global data storage, else {@code false}
	 * @since 1.0.0
	 */
	public final boolean isGlobal() {
		
		return this.global;
	}
	
	/**
	 * Represents an operation that can be executed when updating an entry.
	 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
	 * @version 1.0.0
	 * @since 1.0.0
	 */
	public static enum Operation {

		/**
		 * Adds the value to the existing one (only for integer values)
		 * @since 1.0.0
		 */
		ADD,
		
		/**
		 * Subtracts the value to the existing one (only for integer values)
		 * @since 1.0.0
		 */
		SUBTRACT,
		
		/**
		 * Multiplies the value with the existing one (only for integer values)
		 * @since 1.0.0
		 */
		MULTIPLY,
		
		/**
		 * Divides the existing data with the value (only for integer values)
		 * @since 1.0.0
		 */
		DIVIDE,
		
		/**
		 * Puts the value at the end of the existing data (string operation)
		 * @since 1.0.0
		 */
		APPEND,
		
		/**
		 * Puts the value in front of the existing data (string operation)
		 * @since 1.0.0
		 */
		PREPEND;
	}
}
