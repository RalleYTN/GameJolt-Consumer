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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import de.ralleytn.api.gamejolt.internal.GameJoltObject;
import de.ralleytn.simple.json.JSONObject;
import de.ralleytn.simple.json.JSONParseException;

/**
 * Represents the GameJolt server time.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public final class GameJoltServerTime extends GameJoltObject implements GameJoltRefetchable {

	/*
	 * @param gj the service consumer that created this object
	 * @param json the JSON data of this object
	 * @since 1.0.0
	 */
	GameJoltServerTime(GameJolt gj, JSONObject json) {
		
		super(gj, json);
	}
	
	@Override
	public final void refetch() throws IOException, JSONParseException, GameJoltException {
		
		this.json = this.serviceConsumer.getServerTime().json;
	}
	
	/**
	 * @return the UNIX time stamp in seconds
	 * @since 1.0.0
	 */
	public final Long getTimestamp() {

		return this.json.getLong("timestamp");
	}
	
	/**
	 * @return the time zone
	 * @since 1.0.0
	 */
	public final String getTimeZone() {
		
		return this.json.getString("timezone");
	}
	
	/**
	 * @return the year
	 * @since 1.0.0
	 */
	public final Integer getYear() {
		
		return this.json.getInteger("year");
	}
	
	/**
	 * @return the month of the year starting at {@code 1}
	 * @since 1.0.0
	 */
	public final Integer getMonth() {
		
		return this.json.getInteger("month");
	}
	
	/**
	 * @return the day of the month starting at {@code 1}
	 * @since 1.0.0
	 */
	public final Integer getDay() {
		
		return this.json.getInteger("day");
	}
	
	/**
	 * @return the hour of the day starting at {@code 0}
	 * @since 1.0.0
	 */
	public final Integer getHour() {
		
		return this.json.getInteger("hour");
	}
	
	/**
	 * @return the minute of the hour starting at {@code 0}
	 * @since 1.0.0
	 */
	public final Integer getMinute() {
		
		return this.json.getInteger("minute");
	}
	
	/**
	 * @return the seconds of the minute starting at {@code 0}
	 * @since 1.0.0
	 */
	public final Integer getSeconds() {
		
		return this.json.getInteger("seconds");
	}
	
	/**
	 * @return the server time as an instance of {@linkplain Date}
	 * @since 1.0.0
	 */
	public final Date toDate() {
		
		return new Date(this.getTimestamp() * 1000);
	}
	
	/**
	 * @return the server time as an instance of {@linkplain Instant}
	 * @since 1.0.0
	 */
	public final Instant toInstant() {
		
		return Instant.ofEpochSecond(this.getTimestamp());
	}
	
	/**
	 * @return the server time as an instance of {@linkplain LocalDateTime} (uses the system default time zone)
	 * @since 1.0.0
	 */
	public final LocalDateTime toLocalDateTime() {
		
		return LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault());
	}
	
	/**
	 * @return the server time as an instance of {@linkplain ZonedDateTime} (uses the server time zone)
	 * @since 1.0.0
	 */
	public final ZonedDateTime toZonedDateTime() {
		
		return ZonedDateTime.ofInstant(this.toInstant(), ZoneId.of(this.getTimeZone()));
	}
}
