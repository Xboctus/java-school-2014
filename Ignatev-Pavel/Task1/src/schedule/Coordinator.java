package schedule;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coordinator extends TimerTask implements MessagesAndRegularExpressions{
	
	//Some Commands For Testing->
	//Create(sad,GMT+4,active)
	//Create(das,GMT,active)
	//AddEvent(sad,qwe,29.06.2014-08:45:11)
	//RemoveEvent(sad,qwe)
	//Modify(sad,GMT,passive)
	//ShowInfo(sad)
	//CloneEvent(sad,qwe,das)
	//RemoveEvent(sad,qwe)
	//AddRandomTimeEvent(sad,qwe,29.06.2014-08:54:11,29.06.2014-08:55:11)
	//StartScheduling
	
	//Press Any key to stop Scheduling
	
	private final HashMap<String,User> users = new HashMap();
	
	
	public void start(){
		
		Scanner in = new Scanner(System.in);
		while(true){
			String str = in.next();
			Pattern pattern = Pattern.compile(pCreate);
			Matcher matcher = pattern.matcher(str);
			if (matcher.matches()) {createAction(matcher); continue;}
			
			pattern = Pattern.compile(pModify);
			matcher = pattern.matcher(str);
			if (matcher.matches()) {modifyAction(matcher); continue;}
			
			pattern = Pattern.compile(pAddEvent);
			matcher = pattern.matcher(str);
			if (matcher.matches()) {addEventAction(matcher); continue;}
			
			pattern = Pattern.compile(pAddRandomTimeEvent);
			matcher = pattern.matcher(str);
			if (matcher.matches()) {addRandomTimeEventAction(matcher); continue;}
			
			pattern = Pattern.compile(pRemoveEvent);
			matcher = pattern.matcher(str);
			if (matcher.matches()) {removeEventAction(matcher); continue;}
			
			pattern = Pattern.compile(pCloneEvent);
			matcher = pattern.matcher(str);
			if (matcher.matches()) {cloneEventAction(matcher); continue;}
			
			pattern = Pattern.compile(pShowInfo);
			matcher = pattern.matcher(str);
			if (matcher.matches()) {showInfoAction(matcher); continue;}
			
			pattern = Pattern.compile(pStartScheduling);
			matcher = pattern.matcher(str);
			if (matcher.matches()) {startSchedulingAction(matcher); break;}
			
			System.out.println(msgNoFunction);
			
		}
	}
	


	private void createAction(Matcher matcher){
		String name = matcher.group(gCreate[0]);
		String gmt = matcher.group(gCreate[1]);
		String activness = matcher.group(gCreate[2]);
		
		//Matching ->
		Pattern pattern = Pattern.compile(pGMT);
		matcher = pattern.matcher(gmt);
		if (!matcher.matches()) {System.out.println(msgGmtError);  return;}
		String parseGmt = matcher.group(gGMT[0]);
		pattern = Pattern.compile(pActivness);
		matcher = pattern.matcher(activness);
		if (!matcher.matches()) {System.out.println(msgActiveStatusError);  return;}
		
		
		try{
			int gtmInt;
			if (parseGmt!=null) {
				gtmInt = Integer.parseInt(parseGmt);
				if (!(gtmInt>=-12 && gtmInt<=14)){ System.out.println(msgNoGmt); return;}
			}
		}catch(NumberFormatException e){
			System.out.println(msgGmtError);
			return;
		}
				
		
		if (users.containsKey(name)) {System.out.println(msgNameExists); return;}
		TimeZone timeZone = TimeZone.getTimeZone(gmt);
	
		User user = new User(name);
		user.setTimeZone(timeZone);
		if (activness.equals(activeName)) user.activate();
		users.put(name, user);
		System.out.print(msgDone);
	}
	
	private void modifyAction(Matcher matcher){
		String name = matcher.group(gModify[0]);
		String gmt = matcher.group(gModify[1]);
		String activness = matcher.group(gModify[2]);
		
		//Matching ->
		Pattern pattern = Pattern.compile(pGMT);
		matcher = pattern.matcher(gmt);
		if (!matcher.matches()) {System.out.println(msgGmtError);  return;}
		String parseGmt = matcher.group(gGMT[0]);
		pattern = Pattern.compile(pActivness);
		matcher = pattern.matcher(activness);
		if (!matcher.matches()) {System.out.println(msgActiveStatusError);  return;}
		
		
		
		try{
			int gtmInt;
			if (parseGmt!=null) {
				gtmInt = Integer.parseInt(parseGmt);
				if (!(gtmInt>=-12 && gtmInt<=14)){ System.out.println(msgNoGmt); return;}
			}
		}catch(NumberFormatException e){
			System.out.println(msgGmtError);
			return;
		}
				
		
		if (!users.containsKey(name)) {System.out.println(msgNameNotExists); return;}
		TimeZone timeZone = TimeZone.getTimeZone(gmt);
		
		User user = users.get(name);
		user.setTimeZone(timeZone);
		if (activness.equals(activeName)) user.activate();
		System.out.print(msgDone);
	}

	private void addEventAction(Matcher matcher){
		String name = matcher.group(gAddEvent[0]);
		String text = matcher.group(gAddEvent[1]);
		String dateTime = matcher.group(gAddEvent[2]);
		if (!users.containsKey(name)) {System.out.println(msgNameNotExists); return;}
		User user = users.get(name);
		DateFormat df = new SimpleDateFormat(dateTimeFormat+"z");
		Date date = null;
		try {
			date = df.parse(dateTime+user.getTimeZone().getID());
		} catch (ParseException e) {
			System.out.println(msgDateError);
			return;
		}
		
		if (!user.addEvent(date, text)) System.out.println(msgEventAlreadyExists);
		System.out.print(msgDone);
	}

	private void addRandomTimeEventAction(Matcher matcher){
		String name = matcher.group(gAddRandomTimeEvent[0]);
		String text = matcher.group(gAddRandomTimeEvent[1]);
		String dateTime = matcher.group(gAddRandomTimeEvent[2]);
		String dateTime2 = matcher.group(gAddRandomTimeEvent[3]);
		if (!users.containsKey(name)) {System.out.println(msgNameNotExists); return;}
		User user = users.get(name);
		DateFormat df = new SimpleDateFormat(dateTimeFormat+"z");
		Date date = null;
		try {
			date = df.parse(dateTime+user.getTimeZone().getID());
		} catch (ParseException e) {
			System.out.println(dateTime+":"+msgDateError);
			return;
		}
		Date date2 = null;
		try {
			date2 = df.parse(dateTime2+user.getTimeZone().getID());
		} catch (ParseException e) {
			System.out.println(dateTime2+":"+msgDateError);
			return;
		}
		
		Random rnd = new Random();
		long n = date2.getTime() - date.getTime() + 1;
		long i = rnd.nextLong() % n;
		if (!user.addEvent(new Date(date.getTime()+i), text)) System.out.println(msgEventAlreadyExists);
		System.out.print(msgDone);
	}
	
	private void removeEventAction(Matcher matcher){
		String name = matcher.group(gRemoveEvent[0]);
		String text = matcher.group(gRemoveEvent[1]);
		if (!users.containsKey(name)) {System.out.println(msgNameNotExists); return;}
		User user = users.get(name);
		
		if (!user.removeEvent(text)) System.out.println(msgNoEvent);
		System.out.print(msgDone);
	}
	
	private void cloneEventAction(Matcher matcher){
		String name = matcher.group(gCloneEvent[0]);
		String text = matcher.group(gCloneEvent[1]);
		String name2 = matcher.group(gCloneEvent[2]);
		if (!users.containsKey(name)) {System.out.println(name+":"+msgNameNotExists); return;}
		if (!users.containsKey(name2)) {System.out.println(name2+":"+msgNameNotExists); return;}
		Event event = users.get(name).getEvent(text);
		if (event==null) {System.out.println(msgNoEvent); return;}
		if (!users.get(name2).addEvent(event)) System.out.println(msgEventAlreadyExists);
		System.out.print(msgDone);
		
	}
	
	private void showInfoAction(Matcher matcher){
		String name = matcher.group(gCloneEvent[0]);
		if (!users.containsKey(name)) {System.out.println(name+":"+msgNameNotExists); return;}
		System.out.print(users.get(name).toString());
	}
	
	private void startSchedulingAction(Matcher matcher){
		/*
		 User user = new User("asdasd");
		 users.put("asdasd", user);
		 System.out.println(user.addEvent(new Event(new EventElement(new Date(new Date().getTime()+3000),"a232423"))));
		 System.out.println(user.addEvent(new Event(new EventElement(new Date(new Date().getTime()+3000),"b232423"))));
		 user = new User("basdasd");
		 users.put("basdasd", user);
		 user.addEvent(new Event(new EventElement(new Date(new Date().getTime()+3000),"232423")));
		 */
		
		 
		 Timer timer = new Timer(true);
	     timer.scheduleAtFixedRate(this, 0, 1000);
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     timer.cancel();
	}
	
	
	public static void main(String[] args){
		new Coordinator().start();
	}



	@Override
	public void run() {
		
		HashMap<User,List<Event>> result = new HashMap();
		Iterator it =  users.entrySet().iterator();
		
		while (it.hasNext()) {
			
			Map.Entry pairs = (Map.Entry)it.next();
			User user = (User)pairs.getValue();
			Iterator<Event> uIt =  user.iterator();
			while (uIt.hasNext()) {
				
				Event event = uIt.next();
				
				long diff = new Date().getTime() - event.getElement().getDate().getTime();
				if (diff>0 && diff<1000){
					List<Event> list = null;
					
					if (result.containsKey(user)){
						list = result.get(user);
					}else{
						
						list = new LinkedList();
						result.put(user, list);
					}
					list.add(event);
					
					
					
				}
			}
		}
		
		while(!result.isEmpty()){
			it =  result.entrySet().iterator();
			User next = null;
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				User user = (User)pairs.getKey();
				next = (next==null) ? user :
					((user.getName().compareTo(next.getName())>0) ? user : next);
			}
			Iterator<Event> lIt = result.get(next).iterator();
			Event nextEvent = null;
			while (lIt.hasNext()) {
				Event event = lIt.next();
				nextEvent = (nextEvent==null) ? event :
					((event.getElement().getText().compareTo(nextEvent.getElement().getText())>0) ? event : nextEvent);
			}
			result.get(next).remove(nextEvent);
			if (result.get(next).isEmpty()) result.remove(next);
			System.out.println(new Date().toString() + " " + next.getName() + " " + nextEvent.getElement().getText());
		}
		
	}
}
