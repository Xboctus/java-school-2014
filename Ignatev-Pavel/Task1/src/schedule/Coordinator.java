package schedule;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class Coordinator extends TimerTask implements MessagesAndRegularExpressions, GListener{
	
	
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
	//AddEvent(sad,qwe,02.07.2014-18:14:25)
	
	
	//input output file
	//get data from another schedule socket
	//Press Any key to stop Scheduling
	
	private ConcurrentHashMap<String,User> users = new ConcurrentHashMap();
	private final CListener listener;
	private String incomeMessage;
	private boolean isScheduling = false;
	private boolean isDead = false;
	private final SchedulePerfomer perfomer = new SchedulePerfomer(users) ;
	
	Coordinator(){
		listener = null;
		
	}
	
	Coordinator(CListener listener){
		this.listener=listener;
	}
	
	
	public void start() throws InterruptedException{
		if(listener==null) startConsole();
		else startGraphics();
				
	}
	
	private synchronized void startGraphics() throws InterruptedException{
		while (true){
			while (!isDead && incomeMessage==null){
				wait();
			}
			if (isDead) break;
			String str = incomeMessage;
			incomeMessage = null;
			if (!patternSwitch(str)) break;
			notify();
		}
	}
	private void println(String str,boolean isScheduling){
		System.out.println(str);
		if (listener!=null) listener.addText(str + "\n",isScheduling);
	}
	
	private boolean patternSwitch(String str){
		str = str.replaceAll("\\s","");
		Pattern pattern = Pattern.compile(pCreate);
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {createAction(matcher); return true;}
		
		pattern = Pattern.compile(pModify);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {modifyAction(matcher); return true;}
		
		pattern = Pattern.compile(pAddEvent);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {addEventAction(matcher); return true;}
		
		pattern = Pattern.compile(pAddRandomTimeEvent);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {addRandomTimeEventAction(matcher); return true;}
		
		pattern = Pattern.compile(pRemoveEvent);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {removeEventAction(matcher); return true;}
		
		pattern = Pattern.compile(pCloneEvent);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {cloneEventAction(matcher); return true;}
		
		pattern = Pattern.compile(pShowInfo);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {showInfoAction(matcher); return true;}
		
		pattern = Pattern.compile(pStartScheduling);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {startSchedulingAction(matcher); return true;}
		
		
		pattern = Pattern.compile(pSave);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {save(matcher); return true;}
		
		pattern = Pattern.compile(pLoad);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {load(matcher); return true;}
		
		pattern = Pattern.compile(pRunLikeServer);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {runLikeServer(matcher); return true;}
		
		pattern = Pattern.compile(pSynchWithServer_adress);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {synchWithServer_adress(matcher); return true;}
		
		pattern = Pattern.compile(pSynchWithServer);
		matcher = pattern.matcher(str);
		if (matcher.matches()) {synchWithServer(matcher); return true;}
		
		println(msgNoFunction,false);
		return true;
	}
	
	private void startConsole(){
		
		Scanner in = new Scanner(System.in);
		while(true){
			String str = in.next();
			if (!patternSwitch(str)) break;
		}
	}
	


	private void createAction(Matcher matcher){
		String name = matcher.group(gCreate[0]);
		String gmt = matcher.group(gCreate[1]);
		String activness = matcher.group(gCreate[2]);
		
		//Matching ->
		Pattern pattern = Pattern.compile(pGMT);
		matcher = pattern.matcher(gmt);
		if (!matcher.matches()) {println(msgGmtError,false);  return;}
		String parseGmt = matcher.group(gGMT[0]);
		pattern = Pattern.compile(pActivness);
		matcher = pattern.matcher(activness);
		if (!matcher.matches()) {println(msgActiveStatusError,false);  return;}
		
		
		try{
			int gtmInt;
			if (parseGmt!=null) {
				gtmInt = Integer.parseInt(parseGmt);
				if (!(gtmInt>=-12 && gtmInt<=14)){ println(msgNoGmt,false); return;}
			}
		}catch(NumberFormatException e){
			println(msgGmtError,false);
			return;
		}
				
		
		if (users.containsKey(name)) {println(msgNameExists,false); return;}
		TimeZone timeZone = TimeZone.getTimeZone(gmt);
	
		User user = new User(name);
		user.setTimeZone(timeZone);
		if (activness.equals(activeName)) user.activate();
		users.put(name, user);
		println(msgDone,false);
	}
	
	private void modifyAction(Matcher matcher){
		String name = matcher.group(gModify[0]);
		String gmt = matcher.group(gModify[1]);
		String activness = matcher.group(gModify[2]);
		
		//Matching ->
		Pattern pattern = Pattern.compile(pGMT);
		matcher = pattern.matcher(gmt);
		if (!matcher.matches()) {println(msgGmtError,false);  return;}
		String parseGmt = matcher.group(gGMT[0]);
		pattern = Pattern.compile(pActivness);
		matcher = pattern.matcher(activness);
		if (!matcher.matches()) {println(msgActiveStatusError,false);  return;}
		
		
		
		try{
			int gtmInt;
			if (parseGmt!=null) {
				gtmInt = Integer.parseInt(parseGmt);
				if (!(gtmInt>=-12 && gtmInt<=14)){ println(msgNoGmt,false); return;}
			}
		}catch(NumberFormatException e){
			println(msgGmtError,false);
			return;
		}
				
		// TODO Auto-generated method stub
		if (!users.containsKey(name)) {println(msgNameNotExists,false); return;}
		TimeZone timeZone = TimeZone.getTimeZone(gmt);
		
		User user = users.get(name);
		user.setTimeZone(timeZone);
		if (activness.equals(activeName)) user.activate();
		println(msgDone,false);
	}

	private void addEventAction(Matcher matcher){
		String name = matcher.group(gAddEvent[0]);
		String text = matcher.group(gAddEvent[1]);
		String dateTime = matcher.group(gAddEvent[2]);
		if (!users.containsKey(name)) {println(msgNameNotExists,false); return;}
		User user = users.get(name);
		DateFormat df = new SimpleDateFormat(dateTimeFormat+"z");
		Date date = null;
		try {
			date = df.parse(dateTime+user.getTimeZone().getID());
		} catch (ParseException e) {
			println(msgDateError,false);
			return;
		}
		
		if (!user.addEvent(date, text)) println(msgEventAlreadyExists,false);
		println(msgDone,false);
	}

	private void addRandomTimeEventAction(Matcher matcher){
		String name = matcher.group(gAddRandomTimeEvent[0]);
		String text = matcher.group(gAddRandomTimeEvent[1]);
		String dateTime = matcher.group(gAddRandomTimeEvent[2]);
		String dateTime2 = matcher.group(gAddRandomTimeEvent[3]);
		if (!users.containsKey(name)) {println(msgNameNotExists,false); return;}
		User user = users.get(name);
		DateFormat df = new SimpleDateFormat(dateTimeFormat+"z");
		Date date = null;
		try {
			date = df.parse(dateTime+user.getTimeZone().getID());
		} catch (ParseException e) {
			println(dateTime+":"+msgDateError,false);
			return;
		}
		Date date2 = null;
		try {
			date2 = df.parse(dateTime2+user.getTimeZone().getID());
		} catch (ParseException e) {
			println(dateTime2+":"+msgDateError,false);
			return;
		}
		
		Random rnd = new Random();
		long n = date2.getTime() - date.getTime() + 1;
		long i = rnd.nextLong() % n;
		if (!user.addEvent(new Date(date.getTime()+i), text)) println(msgEventAlreadyExists,false);
		println(msgDone,false);
	}
	
	private void removeEventAction(Matcher matcher){
		String name = matcher.group(gRemoveEvent[0]);
		String text = matcher.group(gRemoveEvent[1]);
		if (!users.containsKey(name)) {println(msgNameNotExists,false); return;}
		User user = users.get(name);
		
		if (!user.removeEvent(text)) println(msgNoEvent,false);
		println(msgDone,false);
	}
	
	private void cloneEventAction(Matcher matcher){
		String name = matcher.group(gCloneEvent[0]);
		String text = matcher.group(gCloneEvent[1]);
		String name2 = matcher.group(gCloneEvent[2]);
		if (!users.containsKey(name)) {println(name+":"+msgNameNotExists,false); return;}
		if (!users.containsKey(name2)) {println(name2+":"+msgNameNotExists,false); return;}
		Event event = users.get(name).getEvent(text);
		if (event==null) {println(msgNoEvent,false); return;}
		if (!users.get(name2).addEvent(event)) println(msgEventAlreadyExists,false);
		println(msgDone,false);
		
	}
	
	private void showInfoAction(Matcher matcher){
		final String name = matcher.group(gCloneEvent[0]);
		if (!users.containsKey(name)) {println(name+":"+msgNameNotExists,false); return;}
		println(users.get(name).toString(),false);
		if (listener==null) return;
		new Thread(new Runnable(){

			@Override
			public void run() {
				listener.showInfo(users.get(name));
			}
			
		}).start();
	}
	
	private void startSchedulingAction(Matcher matcher){
		 /*
		 User user = new User("asdasd");
		 user.activate();
		 user.addEvent(new Event(new EventElement(new Date(new Date().getTime()+3000),"232423")));
		 users.put("asdasd", user);
		// println(user.addEvent(new Event(new EventElement(new Date(new Date().getTime()+3000),"a232423"))));
		// println(user.addEvent(new Event(new EventElement(new Date(new Date().getTime()+3000),"b232423"))));
		 user = new User("basdasd");
		 user.activate();
		 users.put("basdasd", user);
		 user.addEvent(new Event(new EventElement(new Date(new Date().getTime()+3000),"232423")));
		 */
		
		//isScheduling = true;
		Timer timer = new Timer(true);
	    timer.scheduleAtFixedRate(this, 0, 1000);
	    
		
	}
	
	
	public static void main(String[] args) throws InterruptedException{
		ScheduleFrame frame =  new ScheduleFrame();
		frame.setVisible(true);
		Coordinator coordinator = new Coordinator(frame);
		frame.setGListener(coordinator);
		coordinator.start();
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
				if (!user.isActive()) continue;
				next = (next==null) ? user :
					((user.getName().compareTo(next.getName())>0) ? user : next);
			}
			if (next==null) continue;
			Iterator<Event> lIt = result.get(next).iterator();
			Event nextEvent = null;
			while (lIt.hasNext()) {
				Event event = lIt.next();
				nextEvent = (nextEvent==null) ? event :
					((event.getElement().getText().compareTo(nextEvent.getElement().getText())>0) ? event : nextEvent);
			}
			result.get(next).remove(nextEvent);
			if (result.get(next).isEmpty()) result.remove(next);
			println(new Date().toString() + " " + next.getName() + " " + nextEvent.getElement().getText(),true);
		}
		
	}

	@Override
	public synchronized void sendMessage(String msg) throws InterruptedException {
		//if (isScheduling) return;
		
		while (incomeMessage!=null){
			wait();
		}
		
		incomeMessage=msg;
		notify();
	}

	@Override
	public synchronized void alertDeath() {
		isDead = true;
		notify();
	}
	
	private void save(Matcher matcher){
		String path = matcher.group(gSave[0]);
		
		perfomer.setSavePath(path);
		perfomer.save();
		String msg = perfomer.flushLog();
		if (msg!=null) println(msg,true); else
			println(msgDone,false);
	}
	private void load(Matcher matcher){
		String path = matcher.group(gLoad[0]);
		
		perfomer.setLoadPath(path);
		ConcurrentHashMap<String,User> in = (ConcurrentHashMap) perfomer.load();
		users = (in!=null) ? in : users;
		String msg =  perfomer.flushLog();
		if (msg!=null) println(msg,true); else
			println(msgDone,false);
	}
	private void runLikeServer(Matcher matcher){
		int port = Integer.parseInt(matcher.group(gLoad[0]));
		
		perfomer.runLikeServer(port);
		String msg =  perfomer.flushLog();
		if (msg!=null) println(msg,true); else
			println(msgDone,false);
	}
	
	private void synchWithServer(Matcher matcher){
		int port;
		try{
			port = Integer.parseInt(matcher.group(gSynchWithServer[0]));
		}catch(NumberFormatException e){
			println(msgNoFunction,false);
			return;
		}
		
		try {
			users = (ConcurrentHashMap<String, User>) perfomer.synchWithServerAndWait(InetAddress.getByName( null ).getHostAddress(), port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String msg =   perfomer.flushLog();
		if (msg!=null) println(msg,true); else
			println(msgDone,false);
	}
	
	private void synchWithServer_adress(Matcher matcher){
		String adress = null;
		try{
			adress = matcher.group(gSynchWithServer_adress[0]);
		}catch(NumberFormatException e){
			println(msgNoFunction,false);
			return;
		}
		int port = Integer.parseInt(matcher.group(gSynchWithServer_adress[1]));
		users = (ConcurrentHashMap<String, User>) perfomer.synchWithServerAndWait(adress, port);
		String msg =   perfomer.flushLog();
		if (msg!=null) println(msg,true); else
			println(msgDone,false);
	}
}
