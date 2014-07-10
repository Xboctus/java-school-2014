package api;

import centralStructure.Event;

public interface EventDao {
	public void putEventToBase(Event e);

	public void removeEventFromBase(Event e);
}
