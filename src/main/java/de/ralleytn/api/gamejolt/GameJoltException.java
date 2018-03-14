package de.ralleytn.api.gamejolt;

/**
 * Represents an exception thrown by the GameJolt API Consumer.
 * @author Ralph Niemitz/RalleYTN(ralph.niemitz@gmx.de)
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameJoltException extends Exception {

	private static final long serialVersionUID = 9036643539778448557L;
	
	private GameJolt consumer;

	/**
	 * @param consumer the {@linkplain GameJolt} instance
	 * @param message the error message
	 * @since 1.0.0
	 */
	public GameJoltException(GameJolt consumer, String message) {
		
		super(String.format("%s %s: %s", consumer.getServiceName(), consumer.getTargetServiceVersion(), message));
	
		this.consumer = consumer;
	}
	
	/**
	 * @param consumer the {@linkplain GameJolt} instance
	 * @param exception the exception that should be wrapped
	 * @since 1.0.0
	 */
	public GameJoltException(GameJolt consumer, Exception exception) {
		
		super(String.format("%s %s: %s", consumer.getServiceName(), consumer.getTargetServiceVersion(), exception.getMessage()));
		
		this.setStackTrace(exception.getStackTrace());
		this.consumer = consumer;
	}
	
	/**
	 * @return  the {@linkplain GameJolt} instance that threw this exception
	 * @since 1.0.0
	 */
	public GameJolt getServiceConsumer() {
		
		return this.consumer;
	}
}