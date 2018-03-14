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

import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import de.ralleytn.api.gamejolt.GameJolt;
import de.ralleytn.simple.json.JSONObject;

/**
 * An abstract representation of an object used in the GameJolt API.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class GameJoltObject {

	protected JSONObject json;
	protected GameJolt serviceConsumer;
	
	protected GameJoltObject(GameJolt serviceConsumer, JSONObject json) {
		
		this.json = json;
		this.serviceConsumer = serviceConsumer;
	}
	
	/**
	 * @return the JSON data of this object
	 * @since 1.0.0
	 */
	public JSONObject getJSON() {
		
		return this.json;
	}

	/**
	 * @return the service consumer that created this object
	 * @since 1.0.0
	 */
	public GameJolt getServiceConsumer() {
		
		return this.serviceConsumer;
	}
	
	/**
	 * Writes this object on a given {@linkplain Writer}.
	 * @param writer the {@linkplain Writer}
	 * @throws IOException if something went wrong while writing
	 * @since 1.0.0
	 */
	public void write(Writer writer) throws IOException {
		
		this.json.write(writer);
	}
	
	@Override
	public String toString() {
		
		return this.json.toString();
	}
	
	/**
	 * @param param the attribute name
	 * @return the value as an instance of {@linkplain LocalDateTime}
	 * @since 1.0.0
	 */
	protected final LocalDateTime getLocalDateTime(String param) {
		
		Long timestamp = this.json.getLong(param);
		return timestamp != null ? LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()) : null;
	}
	
	/**
	 * Replacement for {@link JSONObject#getEnum(String, Class)}.
	 * Has to be done because the GameJolt API doesn't have upper case enums.
	 * @param param the attribute name
	 * @param type the enum class
	 * @param <T> the enum type
	 * @return the value as the enum type
	 * @since 1.0.0
	 */
	protected final <T extends Enum<?>>T getEnum(String param, Class<T> type) {
		
		String value = this.json.getString(param);
		
		if(value != null) {
			
			for(T constant : type.getEnumConstants()) {
				
				if(constant.name().equals(value.toUpperCase())) {
					
					return constant;
				}
			}
		}
		
		return null;
	}
}
