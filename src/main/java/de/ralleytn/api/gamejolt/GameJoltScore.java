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

import java.time.LocalDateTime;

import de.ralleytn.api.gamejolt.internal.GameJoltObject;
import de.ralleytn.simple.json.JSONObject;

/**
 * Represents a score on GameJolt.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class GameJoltScore extends GameJoltObject {

	/*
	 * @param gj the service consumer that created this object
	 * @param json the JSON data of this object
	 * @since 1.0.0
	 */
	GameJoltScore(GameJolt gj, JSONObject json) {
		
		super(gj, json);
	}
	
	/**
	 * @return the displayed score value
	 * @since 1.0.0
	 */
	public final String getScore() {
		
		return this.json.getString("score");
	}
	
	/**
	 * @return the integer value of the score which will be used to sort it in the table
	 * @since 1.0.0
	 */
	public final Integer getSortValue() {
		
		return this.json.getInteger("sort");
	}
	
	/**
	 * @return extra data attached to the score
	 * @since 1.0.0
	 */
	public final String getExtraData() {
		
		return this.json.getString("extra_data");
	}
	
	/**
	 * @return the username of the user who made this score, or {@code null} if this score was made by a guest user
	 * @since 1.0.0
	 */
	public final String getUser() {
		
		return this.json.getString("user");
	}
	
	/**
	 * @return the user id of the user who made this score, or {@code null} if this score was made by a guest user
	 * @since 1.0.0
	 */
	public final Long getUserId() {
		
		return this.json.getLong("user_id");
	}
	
	/**
	 * @return the guest user who made this score, or {@code null} if this score was made by a registered user
	 * @since 1.0.0
	 */
	public final String getGuest() {
		
		return this.json.getString("guest");
	}
	
	/**
	 * @return the moment this score was made
	 * @since 1.0.0
	 */
	public final LocalDateTime getStored() {
		
		return this.getLocalDateTime("stored_timestamp");
	}
}
