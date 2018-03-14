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
import java.time.LocalDateTime;

import de.ralleytn.api.gamejolt.internal.GameJoltObject;
import de.ralleytn.simple.json.JSONObject;
import de.ralleytn.simple.json.JSONParseException;

/**
 * Represents a GameJolt user.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class GameJoltUser extends GameJoltObject implements GameJoltRefetchable {
	
	/*
	 * @param gj the service consumer that created this object
	 * @param json the JSON data of this object
	 * @since 1.0.0
	 */
	GameJoltUser(GameJolt gj, JSONObject json) {
		
		super(gj, json);
	}
	
	@Override
	public final void refetch() throws IOException, JSONParseException, GameJoltException {
		
		this.json = this.serviceConsumer.getUser(this.getId()).json;
	}

	/**
	 * @return the user ID
	 * @since 1.0.0
	 */
	public final Long getId() {
		
		return this.json.getLong("id");
	}
	
	/**
	 * @return the user type
	 * @since 1.0.0
	 */
	public final Type getType() {
		
		return this.getEnum("type", Type.class);
	}
	
	/**
	 * @return the username
	 * @since 1.0.0
	 */
	public final String getUsername() {
		
		return this.json.getString("username");
	}
	
	/**
	 * @return the URL to the avatar of this user
	 * @since 1.0.0
	 */
	public final String getAvatarUrl() {
		
		return this.json.getString("avatar_url");
	}
	
	/**
	 * @return the moment where this user signed up to GameJolt
	 * @since 1.0.0
	 */
	public final LocalDateTime getSignedUp() {
		
		return this.getLocalDateTime("signed_up_timestamp");
	}
	
	/**
	 * @return the moment this user last logged in
	 * @since 1.0.0
	 */
	public final LocalDateTime getLastLoggedIn() {
		
		return this.getLocalDateTime("last_logged_in_timestamp");
	}
	
	/**
	 * @return {@code true} if the user is currently online, else {@code false}
	 * @since 1.0.0
	 */
	public final boolean isOnline() {
		
		return "Online Now".equals(this.json.getString("last_logged_in"));
	}
	
	/**
	 * @return {@code true} if this user was banned
	 * @since 1.0.0
	 */
	public final boolean isBanned() {
		
		return !"Active".equals(this.json.getString("status"));
	}
	
	/**
	 * @return the developer name of this user, or {@code null} if this user is no developer
	 * @since 1.0.0
	 */
	public final String getDeveloperName() {
		
		return this.json.getString("developer_name");
	}
	
	/**
	 * @return the URL to the developer website of this user, or {@code null} if this user is no developer
	 * @since 1.0.0
	 */
	public final String getDeveloperWebsite() {
		
		return this.json.getString("developer_website");
	}
	
	/**
	 * @return the developer description of this user, or {@code null} if this user is no developer
	 * @since 1.0.0
	 */
	public final String getDeveloperDescription() {
		
		return this.json.getString("developer_description");
	}
	
	/**
	 * Represents the type an user can have.
	 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
	 * @version 1.0.0
	 * @since 1.0.0
	 */
	public static enum Type {

		/**
		 * Just the default user
		 * @since 1.0.0
		 */
		USER,
		
		/**
		 * A game developer
		 * @since 1.0.0
		 */
		DEVELOPER,
		
		/**
		 * A moderator
		 * @since 1.0.0
		 */
		MODERATOR,
		
		/**
		 * A site administrator
		 * @since 1.0.0
		 */
		ADMINISTRATOR;
	}
}
