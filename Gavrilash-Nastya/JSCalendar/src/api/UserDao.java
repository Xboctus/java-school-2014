package api;

import centralStructure.User;

public interface UserDao {
	public void putUserToBase(User u);

	public void changeUser(User u);
}
