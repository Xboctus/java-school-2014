package schedule;

import java.util.Date;

final class EventElement implements Cloneable {
	private Date date;
	private String text;
	
	public Date getDate() {
		return (Date)date.clone();
	}

	public String getText() {
		return text;
	}

	EventElement(Date date,String text){
		this.date = (Date)date.clone();
		this.text = text;
	}

	
	
	@Override
	public EventElement clone(){
		return new EventElement((Date)date.clone(),text);
	}
	
}
