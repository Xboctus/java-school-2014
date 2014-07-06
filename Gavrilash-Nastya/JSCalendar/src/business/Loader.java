package business;

import java.io.IOException;
import java.util.Map;

public interface Loader {
	public void writeToSource(String fileName) throws IOException;

	public Map<String, User> readFromSource(String fileName) throws IOException;
}
