package schedule;


public interface CListener {

	void addText(String msg, boolean isSchedule);
	
	void setGListener(GListener gl);
	
	void showInfo(User user);
	
}
