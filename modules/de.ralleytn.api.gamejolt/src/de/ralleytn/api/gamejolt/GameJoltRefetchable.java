package de.ralleytn.api.gamejolt;

import java.io.IOException;

import de.ralleytn.simple.json.JSONParseException;

public interface GameJoltRefetchable {

	public void refetch() throws JSONParseException, IOException, GameJoltException;
}
