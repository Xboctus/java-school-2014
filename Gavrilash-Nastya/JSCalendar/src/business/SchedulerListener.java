package business;

/**
 * Implementation of this interface allows to get messages of scheduler
 * 
 * @author Gavrilash
 * 
 */
public interface SchedulerListener {
	/**
	 * This methods is invoked by scheduler on all his listeners. Event message
	 * contains time and text of event
	 * 
	 * @param eventMessage
	 */
	public void eventMessageReceived(String eventMessage);
}
