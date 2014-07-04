package schedule;

import java.io.Serializable;
import java.util.Date;

final class EventElement implements Cloneable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
