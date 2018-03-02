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
import java.util.HashMap;
import java.util.Map;

import de.ralleytn.simple.json.JSONObject;
import de.ralleytn.simple.json.JSONParseException;

/**
 * Represents a player session.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class GameJoltSession {

	private final GameJolt gj;
	
	/*
	 * @param gj the service consumer this session belongs to
	 * @since 1.0.0
	 */
	GameJoltSession(GameJolt gj) {
		
		this.gj = gj;
	}
	
	// ---------------------------------------------------------------------------------------------
	
	// ==== GET /sessions/ping
	
	/**
	 * Opens a new session.
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final void open() throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("username", this.gj.getUsername());
		params.put("user_token", gj.getUserToken());
		
		JSONObject response = this.gj.get("/sessions/open", params);
		this.gj.checkStatus(response);
	}
	
	// ==== GET /sessions/ping
	
	/**
	 * Pings a running session. Should be done every 30 seconds after opening a session.
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final void ping() throws IOException, GameJoltException, JSONParseException {
		
		this.ping(null);
	}
	
	/**
	 * Pings a running session. Should be done every 30 seconds after opening a session.
	 * @param status the session status
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final void ping(Status status) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("username", this.gj.getUsername());
		params.put("user_token", gj.getUserToken());
		
		if(status != null) {
			
			params.put("status", status.toString().toLowerCase());
		}
		
		JSONObject response = this.gj.get("/sessions/ping", params);
		this.gj.checkStatus(response);
	}
	
	// ==== GET /sessions/check
	
	/**
	 * @return {@code true} if there is still an open session, else {@code false}
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final boolean isOpen() throws IOException, JSONParseException, GameJoltException {
	
		Map<String, Object> params = new HashMap<>();
		params.put("username", this.gj.getUsername());
		params.put("user_token", gj.getUserToken());
		
		return this.gj.get("/sessions/check", params).getBoolean("success");
	}
	
	// ==== GET /sessions/close
	
	/**
	 * Closes a running session.
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public final void close() throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("username", this.gj.getUsername());
		params.put("user_token", gj.getUserToken());
		
		JSONObject response = this.gj.get("/sessions/close", params);
		this.gj.checkStatus(response);
	}
	
	// ---------------------------------------------------------------------------------------------
	
	/**
	 * @return the service consumer this session belongs to
	 * @since 1.0.0
	 */
	public final GameJolt getServiceConsumer() {
		
		return this.gj;
	}
	
	/**
	 * Represents the status of a session.
	 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
	 * @version 1.0.0
	 * @since 1.0.0
	 */
	public static enum Status {

		/**
		 * The player is actively playing the game
		 * @since 1.0.0
		 */
		ACTIVE,
		
		/**
		 * Commonly used when the player didn't give input for a while
		 * @since 1.0.0
		 */
		IDLE;
	}
}
