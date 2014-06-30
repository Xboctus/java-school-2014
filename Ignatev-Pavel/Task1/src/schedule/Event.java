package schedule;

class Event implements Cloneable {
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
