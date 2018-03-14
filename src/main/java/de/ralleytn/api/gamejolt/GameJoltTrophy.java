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

import de.ralleytn.api.gamejolt.internal.GameJoltObject;
import de.ralleytn.simple.json.JSONObject;
import de.ralleytn.simple.json.JSONParseException;

/**
 * Represents a trophy/an achievement on GameJolt.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class GameJoltTrophy extends GameJoltObject implements GameJoltRefetchable {
	
	GameJoltTrophy(GameJolt gj, JSONObject json) {
		
		super(gj, json);
	}
	
	@Override
	public final void refetch() throws IOException, JSONParseException, GameJoltException {
		
		this.json = this.serviceConsumer.getTrophy(this.getId()).json;
	}

	/**
	 * @return the trophy ID
	 * @since 1.0.0
	 */
	public final Long getId() {
		
		return this.json.getLong("id");
	}
	
	/**
	 * @return the trophy title
	 * @since 1.0.0
	 */
	public final String getTitle() {
		
		return this.json.getString("title");
	}
	
	/**
	 * @return the description
	 * @since 1.0.0
	 */
	public final String getDescription() {
		
		return this.json.getString("description");
	}
	
	/**
	 * @return the difficulty
	 * @since 1.0.0
	 */
	public final Difficulty getDifficulty() {
		
		return this.getEnum("difficulty", Difficulty.class);
	}
	
	/**
	 * @return the URL to the trophy image
	 * @since 1.0.0
	 */
	public final String getImageUrl() {
		
		return this.json.getString("image_url");
	}
	
	/**
	 * @return {@code true} if the trophy was already achieved by the currently logged in user, else {@code false}
	 * @since 1.0.0
	 */
	public final boolean isAchieved() {
		
		return !"false".equals(this.json.getString("achieved"));
	}
	
	/**
	 * Represents the difficulty of a trophy.
	 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
	 * @version 1.0.0
	 * @since 1.0.0
	 */
	public static enum Difficulty {

		/**
		 * Bronze
		 * @since 1.0.0
		 */
		BRONZE,
		
		/**
		 * Silver
		 * @since 1.0.0
		 */
		SILVER,
		
		/**
		 * Gold
		 * @since 1.0.0
		 */
		GOLD,
		
		/**
		 * Platinum
		 * @since 1.0.0
		 */
		PLATINUM;
	}
}
