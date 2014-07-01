package schedule;

public interface GListener {
	void sendMessage(String msg) throws InterruptedException;
	
	void alertDeath();
}
