package de.ralleytn.api.gamejolt.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.ralleytn.api.gamejolt.GameJolt;
import de.ralleytn.api.gamejolt.GameJoltException;
import de.ralleytn.api.gamejolt.GameJoltSession;
import de.ralleytn.api.gamejolt.GameJoltSession.Status;
import de.ralleytn.api.gamejolt.GameJoltUser;
import de.ralleytn.simple.json.JSONParseException;

class GameJoltTest {

	private static GameJolt API;
	private static GameJoltSession SESSION;
	private static ScheduledExecutorService SCHEDULER;
	
	private static final void checkUser(GameJoltUser user, String expectedName, GameJoltUser.Type expectedType) {
		
		assertNotNull(user);
		assertEquals(expectedName, user.getUsername());
		assertEquals(expectedType, user.getType());
	}
	
	@BeforeAll
	static void create() {
		
		API = new GameJolt(326317, "b98dd3b1ae8ae61b569f4ab782e9cec7");
		
		try {
			
			// LOGIN
			API.login("GameJoltConsumerTest", "mRftPQ");
			assertTrue(API.isUserLoggedIn());
			
			// START NEW SESSION
			SESSION = API.getSession();
			
			if(SESSION.isOpen()) {
				
				SESSION.close();
			}
			
			SESSION.open();
			
			// CREATE A SCHEDULER TO UPDATE THE SESSION EVERY 25 SECONDS
			SCHEDULER = Executors.newScheduledThreadPool(1);
			SCHEDULER.scheduleAtFixedRate(() -> {
				
				try {
					
					SESSION.ping(Status.ACTIVE);
					
				} catch (IOException | GameJoltException | JSONParseException exception) {
					
					fail(exception.getClass().getName() + ": " + exception.getMessage());
				}
			
			}, 25, 25, TimeUnit.SECONDS);
			
		} catch(IOException | GameJoltException | JSONParseException exception) {
			
			fail(exception.getClass().getName() + ": " + exception.getMessage());
		}
	}
	
	@Test
	void testUsers() {
		
		try {

			GameJoltUser loggedInUser = API.getUser();
			GameJoltUser ralleytnByName = API.getUser("RalleYTN");
			GameJoltUser ralleytnByID = API.getUser(ralleytnByName.getId());
			
			assertEquals(ralleytnByID.toString(), ralleytnByName.toString());
			checkUser(loggedInUser, "GameJoltConsumerTest", GameJoltUser.Type.DEVELOPER);
			checkUser(ralleytnByID, "RalleYTN", GameJoltUser.Type.DEVELOPER);
			checkUser(ralleytnByName, "RalleYTN", GameJoltUser.Type.DEVELOPER);
			
		} catch(IOException | GameJoltException | JSONParseException exception) {

			fail(exception.getClass().getName() + ": " + exception.getMessage());
		}
	}
	
	@AfterAll
	static void destroy() {
		
		SCHEDULER.shutdown();
		API.logout();
	}
}
