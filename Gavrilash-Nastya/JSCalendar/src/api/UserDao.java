package api;

import java.io.IOException;
import java.util.Map;

import centralStructure.User;

public interface UserDao {
	public void putUserToBase(User u);

	public void changeUser(User u);

	public void setUserMapFromBase() throws IOException;
}
