package schedule;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class SchedulePerfomer {
	
	private static final  String errDataFromServer = "Data got from server is corrupted";
	private static final  String errServerNotResponde =  "Server doen't responde"; 
	private static final  String errNotASchedule =  "File is not a Schedule or corrupted";
	private static final  String errCantOpen =  "Can't open a file";
	private static final  String errIOE =  "IOException occured";
	private static final  String errNoInPath =  "Input file is'nt specified";
	private static final  String errNoOutPath =  "Output file is'nt specified";
	private static final  String errNoExc =  "Cannot perform output";
	private static final  String errPort =  "port is not free";
	private static final  String errConnection =  "Can't establish connection";
	private static final  String errConnectionIO =  "Can't establish IO with connection";
	private static final  String errUnknownHost = "Host is unknown";
	
			
	private final List<String> log = Collections.synchronizedList(new LinkedList());
	private Map<String,User> users;
	
	private String savePath;
	private String loadPath;
	private ServerSocket server;
	
	//Client
	private Socket client;
	private ObjectOutput output;
	private ObjectInput input;
	
	SchedulePerfomer(){}
	
	SchedulePerfomer(Map<String,User> users){
		this.users = users;
	}
	
	public String flushLog(){
		if (log.isEmpty()) return null;
		String out = "";
		boolean newLine = false;
		for(String elem : log){
			if (newLine) out = out + "/n";
			log.remove(elem);
			out = out + elem;
			newLine = true;
		}
		return out;
	}
	
	private void addToLog(String str){
		
		log.add(new String(str));
	}
	
	
	public void setUsers(Map<String,User> users){
		this.users = users;
	}
	
	public void save(){
		if (users==null) return;
		if (savePath==null) addToLog(errNoOutPath);
		try {
			OutputStream file = new FileOutputStream(savePath);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(users);
			output.flush();
		}  
		catch(IOException ex){
			//ex.printStackTrace();
			addToLog(errNoExc);
		}
	}
	public  Map<String,User> load(){
		if (loadPath==null) {addToLog(errNoInPath); return null;}
		try {
			InputStream file = new FileInputStream(loadPath);
		    InputStream buffer = new BufferedInputStream(file);
		    ObjectInput input = new ObjectInputStream (buffer);
		    Map<String,User> out = (Map<String,User>) input.readObject();
		    users = out;
		    return out;
		}  
		catch(ClassNotFoundException ex){
			addToLog(errNotASchedule);
			// ex.printStackTrace();
		}
		catch(IOException ex){
			addToLog(errCantOpen);
		   	//ex.printStackTrace();
		}
		return null;
		
	}

	
		
	public Map<String,User> synchWithServerAndWait(String name, int port){
		try {
			if (client==null || client.getPort()!=port){
				input = null;
				output = null;
				client = new Socket(name, port);
			}
		} catch (UnknownHostException e1) {
			addToLog(errUnknownHost);
			return null;
			//e1.printStackTrace();
		} catch (IOException e1) {
			addToLog(errConnectionIO);
			return null;
			//e1.printStackTrace();
		}
		
		try {
			OutputStream buffer = new BufferedOutputStream(client.getOutputStream());
			if (output==null)
			output = new ObjectOutputStream(buffer);
			output.writeObject(new Integer(1));
			output.flush();
			InputStream iBuffer = new BufferedInputStream(client.getInputStream());
			if (input==null)
			input = new ObjectInputStream (iBuffer);
			Map<String,User> fromServer = null;
			try {
				fromServer = (Map<String,User>)input.readObject();
			} catch (ClassNotFoundException e) {
				addToLog(errDataFromServer);
				return null;
				//e.printStackTrace();
			}
			users = fromServer;
			return fromServer;
			
			
		} catch (IOException e) {
			addToLog(errConnectionIO+": "+client.toString());
		}
		return null;
	}
	
	
	public void runLikeServer(final int port){
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					server = new ServerSocket(port);
				} catch (IOException e) {
					//e.printStackTrace();
					addToLog(((Integer)port).toString()+" "+errPort);
				}
				while (true) {
					createConnection();
				}
			}
			
		}).start();
	}	
	private void createConnection(){
		
		try {
			final Socket client = server.accept();
			new Thread(new Runnable(){

				@Override
				public void run() {
					
					try {
						OutputStream buffer = new BufferedOutputStream(client.getOutputStream());
						ObjectOutput output = new ObjectOutputStream(buffer);
						InputStream iBuffer = new BufferedInputStream(client.getInputStream());
						ObjectInput input = new ObjectInputStream (client.getInputStream());
						while(true){
							input.readObject();
							output.writeObject(users);
							output.flush();
						}
						
					} catch (IOException | ClassNotFoundException e) {
						//e.printStackTrace();
						addToLog(errConnectionIO+": "+client.toString());
					}
				}
				
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
			addToLog(errConnection);
		} catch (NullPointerException e) {
			e.printStackTrace();
			addToLog(errPort);
			
		}
	}
	
		
	public String getSavePath() {
		return savePath;
	}



	public void setSavePath(String savePath) {
		this.savePath = savePath;
		
	}



	public String getLoadPath() {
		return loadPath;
	}



	public void setLoadPath(String loadPath) {
		this.loadPath = loadPath;
	}



}
