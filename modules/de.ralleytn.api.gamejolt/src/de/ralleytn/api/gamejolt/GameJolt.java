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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ralleytn.api.gamejolt.internal.GameJoltObject;
import de.ralleytn.api.gamejolt.internal.Utils;
import de.ralleytn.simple.json.JSONArray;
import de.ralleytn.simple.json.JSONObject;
import de.ralleytn.simple.json.JSONParseException;
import de.ralleytn.simple.json.JSONParser;

/**
 * Represents the interface between the client program and GameJolt.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameJolt {

	private static final String SERVICE_NAME = "GameJolt API";
	private static final String TARGET_VERSION = "1.1";
	private static final String PROTOCOL = "https";
	private static final String DOMAIN = "gamejolt.com";
	private static final String SERVICE_URL = "/api/game/v1_1";
	
	private int gameId;
	private String privateKey;
	private GameJoltSession session;
	private GameJoltDataStorage globalStorage;
	private GameJoltDataStorage userStorage;
	private String username;
	private String user_token;
	
	/**
	 * @param gameId the game ID
	 * @param privateKey the private key of the game
	 * @since 1.0.0
	 */
	public GameJolt(int gameId, String privateKey) {
		
		this.gameId = gameId;
		this.privateKey = privateKey;
		this.session = new GameJoltSession(this);
		this.globalStorage = new GameJoltDataStorage(this, true);
		this.userStorage = new GameJoltDataStorage(this, false);
	}
	
	/**
	 * @return the global data storage
	 * @since 1.0.0
	 */
	public GameJoltDataStorage getGlobalDataStorage() {
		
		return this.globalStorage;
	}
	
	/**
	 * @return the data storage for the currently logged in user
	 * @since 1.0.0
	 */
	public GameJoltDataStorage getUserDataStorage() {
		
		return this.userStorage;
	}
	
	/**
	 * @return the session for the currently logged in user
	 * @since 1.0.0
	 */
	public GameJoltSession getSession() {
		
		return this.session;
	}
	
	/**
	 * @return the service name
	 * @since 1.0.0
	 */
	public String getServiceName() {
		
		return GameJolt.SERVICE_NAME;
	}
	
	/**
	 * @return the target service version
	 * @since 1.0.0
	 */
	public String getTargetServiceVersion() {
		
		return GameJolt.TARGET_VERSION;
	}
	
	/**
	 * Logs out the currently logged in user.
	 * @since 1.0.0
	 */
	public void logout() {
		
		this.username = null;
		this.user_token = null;
	}
	
	/**
	 * @return {@code true} if a user is logged in, else {@code false}
	 * @since 1.0.0
	 */
	public boolean isUserLoggedIn() {
		
		return this.user_token != null;
	}
	
	// ---------------------------------------------------------------------------------------------
	
	// ==== GET /trophies/add-achieved
	
	/**
	 * Achieves a trophy.
	 * @param trophy_id the trophy ID
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void achieveTrophy(long trophy_id) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("username", this.username);
		params.put("user_token", this.user_token);
		params.put("trophy_id", trophy_id);
		
		JSONObject response = this.get("/trophies/add-achieved", params);
		this.checkStatus(response);
	}
	
	// ==== GET /scores/tables
	
	/**
	 * @return the score tables
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltScoreTable> getScoreTables() throws IOException, GameJoltException, JSONParseException {
		
		JSONObject response = this.get("/scores/tables", null);
		this.checkStatus(response);
		return GameJolt.toList(response.getArray("tables"), GameJoltScoreTable.class, this);
	}
	
	// ==== GET /scores/get-rank
	
	/**
	 * @param sort the sort value of a score
	 * @return the rank for the given sort value on the primary table
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public Integer getRank(int sort) throws IOException, GameJoltException, JSONParseException {
		
		return this.getRank(sort, null);
	}
	
	/**
	 * @param sort the sort value of a score
	 * @param table_id the ID of the table on which you want to check the rank
	 * @return the rank for the given sort value
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public Integer getRank(int sort, long table_id) throws IOException, GameJoltException, JSONParseException {
		
		return this.getRank(sort, (Long)table_id);
	}

	private Integer getRank(int sort, Long table_id) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("sort", sort);
		
		if(table_id != null) {
			
			params.put("table_id", table_id);
		}
		
		JSONObject response = this.get("/scores/get-rank", params);
		this.checkStatus(response);
		return response.getInteger("rank");
	}
	
	// ==== GET|POST /scores/add
	
	/**
	 * Adds a new score for the currently logged in user.
	 * @param score the display value of the score
	 * @param sort the sort value of the score
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void addUserScore(String score, int sort) throws IOException, GameJoltException, JSONParseException {
		
		this.addScore(score, sort, null, null, null);
	}
	
	/**
	 * Adds a new score for the currently logged in user.
	 * @param score the display value of the score
	 * @param sort the sort value of the score
	 * @param extra_data some extra data
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void addUserScore(String score, int sort, String extra_data) throws IOException, GameJoltException, JSONParseException {
		
		this.addScore(score, sort, null, null, extra_data);
	}
	
	/**
	 * Adds a new score for the currently logged in user.
	 * @param score the display value of the score
	 * @param sort the sort value of the score
	 * @param table_id the ID of the table this score should be added to
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void addUserScore(String score, int sort, long table_id) throws IOException, GameJoltException, JSONParseException {
		
		this.addScore(score, sort, table_id, null, null);
	}
	
	/**
	 * Adds a new score for the currently logged in user.
	 * @param score the display value of the score
	 * @param sort the sort value of the score
	 * @param table_id the ID of the table this score should be added to
	 * @param extra_data some extra data
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void addUserScore(String score, int sort, long table_id, String extra_data) throws IOException, GameJoltException, JSONParseException {

		this.addScore(score, sort, table_id, null, extra_data);
	}
	
	/**
	 * Adds a new score for a guest user.
	 * @param score the display value of the score
	 * @param sort the sort value of the score
	 * @param guest the name of the guest user who made this score
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void addGuestScore(String score, int sort, String guest) throws IOException, GameJoltException, JSONParseException {

		this.addScore(score, sort, null, guest, null);
	}
	
	/**
	 * Adds a new score for a guest user.
	 * @param score the display value of the score
	 * @param sort the sort value of the score
	 * @param guest the name of the guest user who made this score
	 * @param extra_data some extra data
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void addGuestScore(String score, int sort, String guest, String extra_data) throws IOException, GameJoltException, JSONParseException {

		this.addScore(score, sort, null, guest, extra_data);
	}
	
	/**
	 * Adds a new score for a guest user.
	 * @param score the display value of the score
	 * @param sort the sort value of the score
	 * @param guest the name of the guest user who made this score
	 * @param table_id the ID of the table this score should be added to
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void addGuestScore(String score, int sort, String guest, long table_id) throws IOException, GameJoltException, JSONParseException {
	
		this.addScore(score, sort, table_id, guest, null);
	}
	
	/**
	 * Adds a new score for a guest user.
	 * @param score the display value of the score
	 * @param sort the sort value of the score
	 * @param guest the name of the guest user who made this score
	 * @param table_id the ID of the table this score should be added to
	 * @param extra_data some extra data
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void addGuestScore(String score, int sort, String guest, long table_id, String extra_data) throws IOException, GameJoltException, JSONParseException {
		
		this.addScore(score, sort, table_id, guest, extra_data);
	}

	private final void addScore(String score, int sort, Long table_id, String guest, String extra_data) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> postParams = new HashMap<>();
		postParams.put("extra_data", extra_data);
		
		Map<String, Object> params = new HashMap<>();
		params.put("score", score);
		params.put("sort", sort);
		
		if(table_id != null) {
			
			params.put("table_id", table_id);
		}
		
		if(guest != null) {
			
			params.put("guest", guest);
			
		} else {
			
			params.put("username", this.username);
			params.put("user_token", this.user_token);
		}
		
		final String endpoint = "/scores/add";
		JSONObject response = extra_data != null ? this.post(endpoint, params, postParams) : this.get(endpoint, params);
		this.checkStatus(response);
	}
	
	// ==== GET /scores
	
	/**
	 * @return the top ten scores from the primary table
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltScore> getScores() throws IOException, GameJoltException, JSONParseException {
		
		return this.getScores(false, null, null);
	}
	
	/**
	 * @param limit the limit of scores that should be returned (1 - 100)
	 * @return a list of scores from the primary table
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltScore> getScores(int limit) throws IOException, GameJoltException, JSONParseException {
		
		return this.getScores(false, limit, null);
	}
	
	/**
	 * @param table_id the ID of the table from which the scores should be fetched
	 * @return the top ten scores from the given table
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltScore> getScores(long table_id) throws IOException, GameJoltException, JSONParseException {
		
		return this.getScores(false, null, table_id);
	}
	
	/**
	 * @param table_id the ID of the table from which the scores should be fetched
	 * @param limit the limit of scores that should be returned (1 - 100)
	 * @return a list of scores from the given table
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltScore> getScores(long table_id, int limit) throws IOException, GameJoltException, JSONParseException {
		
		return this.getScores(false, limit, table_id);
	}
	
	/**
	 * @return the top ten scores of the currently logged in user on the primary table
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltScore> getUserScores() throws IOException, GameJoltException, JSONParseException {
		
		return this.getScores(true, null, null);
	}
	
	/**
	 * @param limit the limit of scores that should be returned (1 - 100)
	 * @return a list of scores for the currently logged in user from the primary table
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltScore> getUserScores(int limit) throws IOException, GameJoltException, JSONParseException {
		
		return this.getScores(true, limit, null);
	}
	
	/**
	 * @param table_id the ID of the table from which the scores should be fetched
	 * @return the top ten scores of the currently logged in user on the given table
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltScore> getUserScores(long table_id) throws IOException, GameJoltException, JSONParseException {
		
		return this.getScores(true, null, table_id);
	}
	
	/**
	 * @param table_id the ID of the table from which the scores should be fetched
	 * @param limit the limit of scores that should be returned (1 - 100)
	 * @return a list of scores for the currently logged in user from the given table
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltScore> getUserScores(long table_id, int limit) throws IOException,GameJoltException, JSONParseException {
		
		return this.getScores(true, limit, table_id);
	}

	private final List<GameJoltScore> getScores(boolean user, Integer limit, Long table_id) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		
		if(user) {
			
			params.put("user_token", this.user_token);
			params.put("username", this.username);
		}
		
		if(limit != null) {
			
			params.put("limit", limit);
		}
		
		if(table_id != null) {
			
			params.put("table_id", table_id);
		}
		
		JSONObject response = this.get("/scores", params);
		this.checkStatus(response);
		return GameJolt.toList(response.getArray("scores"), GameJoltScore.class, this);
	}
	
	// ==== GET /get-time
	
	/**
	 * @return the current server time
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public GameJoltServerTime getServerTime() throws IOException, GameJoltException, JSONParseException {
		
		JSONObject response = this.get("/get-time", null);
		this.checkStatus(response);
		response.remove("success");
		return new GameJoltServerTime(this, response);
	}
	
	// ==== GET /trophies
	
	/**
	 * @return a list of trophies
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltTrophy> getTrophies() throws IOException, GameJoltException, JSONParseException {
		
		return this.getTrophies(null, null);
	}
	
	/**
	 * @param achieved {@code true} if only trophies that were already achieved by this user should be returned, {@code false} if only those that aren't achieved by the user should be returned
	 * @return a list of trophies
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltTrophy> getTrophies(boolean achieved) throws IOException, GameJoltException, JSONParseException {
		
		return this.getTrophies(achieved, null);
	}
	
	/**
	 * @param trophy_ids the trophy IDs
	 * @return the trophies with the given IDs
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltTrophy> getTrophies(Collection<Long> trophy_ids) throws IOException, GameJoltException, JSONParseException {
		
		return this.getTrophies(Utils.toLongArray(trophy_ids));
	}
	
	/**
	 * @param trophy_ids the trophy IDs
	 * @return the trophies with the given IDs
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltTrophy> getTrophies(long[] trophy_ids) throws IOException, GameJoltException, JSONParseException {
		
		return this.getTrophies(null, trophy_ids);
	}
	
	/**
	 * @param trophy_id the trophy ID
	 * @return the trophy with the given ID
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public GameJoltTrophy getTrophy(long trophy_id) throws IOException, GameJoltException, JSONParseException {
		
		return this.getTrophies(new long[] {trophy_id}).get(0);
	}

	private final List<GameJoltTrophy> getTrophies(Boolean achieved, long[] trophy_ids) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("user_token", this.user_token);
		params.put("username", this.username);
		
		if(achieved != null) {
			
			params.put("achieved", achieved);
		}
		
		if(trophy_ids != null) {
			
			params.put(trophy_ids.length > 1 ? "trophy_ids" : "trophy_id", trophy_ids);
		}
		
		JSONObject response = this.get("/trophies", params);
		this.checkStatus(response);
		return GameJolt.toList(response.getArray("trophies"), GameJoltTrophy.class, this);
	}
	
	// ==== GET /users
	
	/**
	 * @param user_ids the user IDs
	 * @return the users with the given IDs
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltUser> getUsers(Collection<Long> user_ids) throws IOException, GameJoltException, JSONParseException {
		
		return this.getUsers(Utils.toLongArray(user_ids));
	}
	
	/**
	 * @param user_ids the user IDs
	 * @return the users with the given IDs
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public List<GameJoltUser> getUsers(long[] user_ids) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("user_ids", user_ids);
		
		JSONObject response = this.get("/users", params);
		this.checkStatus(response);
		return GameJolt.toList(response.getArray("users"), GameJoltUser.class, this);
	}
	
	/**
	 * @param user_id the user ID
	 * @return the user with the given ID
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public GameJoltUser getUser(long user_id) throws IOException, GameJoltException, JSONParseException {
		
		return this.getUsers(new long[] {user_id}).get(0);
	}
	
	/**
	 * @param username the username
	 * @return the user with the given username
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public GameJoltUser getUser(String username) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		
		JSONObject response = this.get("/users", params);
		this.checkStatus(response);
		return new GameJoltUser(this, response.getArray("users").getObject(0));
	}
	
	/**
	 * @return the currently logged in user
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public GameJoltUser getUser() throws IOException, GameJoltException, JSONParseException {
		
		return this.getUser(this.username);
	}
	
	// ==== GET /users/auth
	
	/**
	 * Authenticates a user. If authentication failed a {@linkplain ServiceException} will be thrown.
	 * @param username the username
	 * @param user_token the token of the user
	 * @throws IOException if something went wrong during the data transfer
	 * @throws JSONParseException if the JSON data could not be parsed
	 * @throws GameJoltException if the service says something went wrong
	 * @since 1.0.0
	 */
	public void login(String username, String user_token) throws IOException, GameJoltException, JSONParseException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		params.put("user_token", user_token);
		
		JSONObject response = this.get("/users/auth", params);
		this.checkStatus(response);

		this.username = username;
		this.user_token = user_token;
	}
	
	// ---------------------------------------------------------------------------------------------

	final void checkStatus(JSONObject object) throws GameJoltException {
		
		if(!object.getBoolean("success")) {
			
			throw new GameJoltException(this, object.getString("message"));
		}
	}

	final String getUsername() {
		
		return this.username;
	}
	
	final String getUserToken() {
		
		return this.user_token;
	}
	
	static final <T extends GameJoltObject>List<T> toList(JSONArray array, Class<T> type, GameJolt consumer) {
		
		List<T> list = new ArrayList<>();
		
		for(Object element : array) {
			
			try {
				
				Constructor<T> constructor = type.getDeclaredConstructor(GameJolt.class, JSONObject.class);
				list.add(constructor.newInstance(consumer, (JSONObject)element));
			
			} catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException exception) {
				
				// SHOULD NEVER HAPPEN!
				throw new RuntimeException(exception);
			}
		}
		
		return list;
	}

	private final JSONObject finishRequest(HttpURLConnection connection) throws GameJoltException, IOException, JSONParseException {
		
		int status = connection.getResponseCode();
		
		if(status == HttpURLConnection.HTTP_OK) {
			
			try(BufferedReader reader = GameJolt.createReader(connection.getInputStream())) {
				
				Object parsedObject = new JSONParser().parse(reader);
				
				if(parsedObject instanceof JSONObject) {
					
					return ((JSONObject)parsedObject).getObject("response");
					
				} else {
					
					throw new GameJoltException(this, "A JSON object was expected, not an array!");
				}
			}
			
		} else {
			
			throw new GameJoltException(this, String.format("%d %s: %s", status, connection.getResponseMessage(), GameJolt.read(connection.getErrorStream())));
		}
	}

	final JSONObject post(String endpoint, Map<String, Object> params, Map<String, Object> postParams) throws IOException, GameJoltException, JSONParseException {

		HttpURLConnection connection = GameJolt.createConnection(this.createURL(endpoint, params), "POST", true);
		GameJolt.write(connection.getOutputStream(), Utils.getQueryString(postParams).substring(1));
		return this.finishRequest(connection);
	}

	final JSONObject get(String endpoint, Map<String, Object> params) throws IOException, GameJoltException, JSONParseException {

		return this.finishRequest(GameJolt.createConnection(this.createURL(endpoint, params), "GET", false));
	}

	private final String createURL(String endpoint, Map<String, Object> params) {
		
		if(params == null) {
			
			params = new HashMap<>();
		}
		
		params.put("game_id", this.gameId);
		params.put("format", "json");
		
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(GameJolt.PROTOCOL);
		urlBuilder.append("://");
		urlBuilder.append(GameJolt.DOMAIN);
		urlBuilder.append(GameJolt.SERVICE_URL);
		urlBuilder.append(endpoint);
		urlBuilder.append(Utils.getQueryString(params));
		
		String signature = Utils.createSignature(urlBuilder.toString(), this.privateKey);
		
		urlBuilder.append("&signature=");
		urlBuilder.append(signature);
		
		return urlBuilder.toString();
	}
	
	private static final String read(InputStream inputStream) throws IOException {
		
		try(BufferedReader reader = GameJolt.createReader(inputStream)) {
			
			StringBuilder contentBuilder = new StringBuilder();
			String line = null;
			
			while((line = reader.readLine()) != null) {
				
				contentBuilder.append(line);
				contentBuilder.append('\n');
			}
			
			return contentBuilder.toString();
		}
	}
	
	private static final HttpURLConnection createConnection(String requestURL, String requestMethod, boolean doOutput) throws IOException {

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
	
	private static final void write(OutputStream outputStream, String content) throws IOException {
		
		try(BufferedWriter writer = GameJolt.createWriter(outputStream)) {
			
			writer.write(content);
			writer.flush();
		}
	}
	
	private static final BufferedReader createReader(InputStream inputStream) {
		
		return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}
	
	private static final BufferedWriter createWriter(OutputStream outputStream) {
		
		return new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
	}
}
