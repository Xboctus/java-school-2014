package impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import api.EventDao;
import api.UserDao;
import centralStructure.Event;
import centralStructure.User;

public class ChangeManager {
	private static Map<String, User> newUsers = new HashMap<String, User>();
	private static Map<String, User> modifiedUsers = new HashMap<String, User>();
	private static Map<Integer, Event> newEvents = new HashMap<Integer, Event>();
	private static Map<Integer, Event> removedEvents = new HashMap<Integer, Event>();

	private EventDao eventDao;
	private UserDao userDao;

	public ChangeManager() {
		eventDao = new EventDaoImpl();
		userDao = new UserDaoImpl();
	}

	public void addToNewUsers(User u) {
		newUsers.put(u.getName(), u);
	}

	public void addToModifiedUsers(User u) {
		modifiedUsers.put(u.getName(), u);
	}

	public void addToNewEvents(Event e) {
		newEvents.put(e.getId(), e);
	}

	public void addToRemoveEvents(Event e) {
		removedEvents.put(e.getId(), e);
	}

	public boolean haveUnsavedChanges() {
		return (newUsers.size() == 0 && modifiedUsers.size() == 0
				&& newEvents.size() == 0 && removedEvents.size() == 0);
	}

	public void clearChanges() {
		newUsers.clear();
		modifiedUsers.clear();
		newEvents.clear();
		removedEvents.clear();
	}

	public void saveChangesToBase() {
		for (String s : newUsers.keySet()) {
			userDao.putUserToBase(newUsers.get(s));
		}
		for (String s : modifiedUsers.keySet()) {
			userDao.changeUser(modifiedUsers.get(s));
		}
		for (Integer i : newEvents.keySet()) {
			eventDao.putEventToBase(newEvents.get(i));
		}
		for (Integer i : removedEvents.keySet()) {
			eventDao.removeEventFromBase(removedEvents.get(i));
		}
		clearChanges();
	}

	public void setUserMap() throws IOException {
		clearChanges();
		userDao.setUserMapFromBase();
	}
}
