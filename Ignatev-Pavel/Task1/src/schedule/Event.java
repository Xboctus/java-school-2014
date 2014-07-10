package schedule;

import java.io.Serializable;

class Event implements Cloneable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final EventElement element;
	
	public EventElement getElement() {
		return element;
	}

	Event(EventElement element){
		this.element = element;		
	}
	
	@Override
	public Event clone(){
		return new Event(element);
	}
		
	
}
