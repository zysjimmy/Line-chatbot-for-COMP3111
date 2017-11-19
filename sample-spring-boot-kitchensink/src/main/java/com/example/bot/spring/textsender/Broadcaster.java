package com.example.bot.spring.textsender;
/**
 * interface that all broadcast should implement
 * @author jsongaf
 *
 */
public interface Broadcaster {

	/**
	 * function to be implement by each BroadcasterImp 
	 * called in Listener class
	 * periodically broadcast message to relevant clients upon certain conditions is fulfilled;
	 * */
	public void broadcast() throws Exception;
}
