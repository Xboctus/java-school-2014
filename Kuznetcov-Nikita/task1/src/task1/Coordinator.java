package task1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import task1.DAO.MySQLDAO;
import task1.Util.ResponseStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;

public class Coordinator {

  private ServerSocket srvSocket;
  private Map<String, User> usersMap;
  private MySQLDAO dao;
  private static int logNumber;
  public Logger logger; // TODO trash

  private static final String DBURL = "jdbc:mysql://localhost:3306/scheduler"; // jdbc:mysql://localhost:3306/db_name
  private static final String DBUsername = "root";
  private static final String DBPassword = "admin";

  public Coordinator() {
    try {
      srvSocket = new ServerSocket(0);
      dao = new MySQLDAO(DBURL, DBUsername, DBPassword);
    } catch (IOException ioex) {
      logger.severe("error when creating server socket");
    } catch (ClassNotFoundException cnfe) {
      System.out.println("Driver library not found");
    } catch (SQLException sqlex) {
      System.out.println("Cannot establish connection to db");
      sqlex.printStackTrace();
    }
    usersMap = new ConcurrentSkipListMap<>();

    logger = Logger.getLogger(String.valueOf(logNumber++));
  }

  // scheduler API
  public ResponseStatus addNewUser(String userName, String timeZoneID, boolean status) {
    if (usersMap.containsKey(userName)) {
      return ResponseStatus.USER_ALREADY_EXISTS;
    }
    if (!timeZoneID.matches("GMT[+-][0-2]?[0-9](:[0-5]?[0-9])?")) {
      return ResponseStatus.BAD_TIMEZONE_FORMAT;
    }
    usersMap.put(userName, new User(userName, TimeZone.getTimeZone(timeZoneID), status));
    return ResponseStatus.USER_ADDED;
  }

  public ResponseStatus modifyUser(String userName, String timeZoneID, boolean status) {
    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      return ResponseStatus.USER_NOT_FOUND;
    }
    if (!timeZoneID.matches("GMT[+-][0-2]?[0-9](:[0-5]?[0-9])?")) {
      return ResponseStatus.BAD_TIMEZONE_FORMAT;
    }
    targetUser.setTimeZone(TimeZone.getTimeZone(timeZoneID));
    targetUser.setActive(status);
    return ResponseStatus.USER_MODIFIED;
  }

  public ResponseStatus addEvent(String userName, String taskText, String taskDateInput) {
    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      return ResponseStatus.USER_NOT_FOUND;
    }
    Date date = DateFormatter.parseDate(taskDateInput, targetUser.getTimeZone());
    if (date == null) {
      return ResponseStatus.BAD_DATE_FORMAT;
    }
    if (!targetUser.addEvent(new Event(DateFormatter.parseDate(taskDateInput, targetUser.getTimeZone()), taskText))) {
      return ResponseStatus.EVENT_ALREADY_EXISTS;
    }
    return ResponseStatus.EVENT_ADDED;
  }

  public ResponseStatus removeEvent(String userName, String targetText) {
    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      return ResponseStatus.USER_NOT_FOUND;
    }
    Event targetEvent = targetUser.getEventByText(targetText);
    if (targetEvent == null) {
      return ResponseStatus.EVENT_NOT_FOUND;
    }
    targetUser.removeEvent(targetUser.getEventByText(targetText));
    return ResponseStatus.EVENT_REMOVED;
  }

  public ResponseStatus addRandomTimeEvent(String userName, String taskText, String dateFromInput, String dateToInput) {

    User targetUser = usersMap.get(userName);
    if (targetUser == null) {
      return ResponseStatus.USER_NOT_FOUND;
    }

    Date dateTo = DateFormatter.parseDate(dateToInput, targetUser.getTimeZone());
    Date dateFrom = DateFormatter.parseDate(dateFromInput, targetUser.getTimeZone());
    long diff = dateTo.getTime() - dateFrom.getTime();
    if (diff <= 0) return ResponseStatus.WRONG_DATE_DIFFERENCE;

    Random generator = new Random();
    long addition;
    if (diff > Integer.MAX_VALUE) {
      addition = diff - generator.nextInt();
    } else {
      // else we choose any int from [0, diff)
      addition = generator.nextInt((int) diff);
    }

    if (targetUser.addEvent(new Event(new Date(dateFrom.getTime() + addition), taskText))) {
      return ResponseStatus.EVENT_ADDED;
    } else {
      return ResponseStatus.EVENT_NOT_FOUND;
    }
  }

  public ResponseStatus cloneEvent(String userName, String taskText, String targetUserName) {
    try {
      User srcUser = usersMap.get(userName);
      User targetUser = usersMap.get(targetUserName);
      if (srcUser == null) {
        return ResponseStatus.USER_NOT_FOUND;
      }
      if (targetUser == null) {
        return ResponseStatus.TARGET_USER_NOT_FOUND;
      }
      Event srcEvent = srcUser.getEventByText(taskText);
      if (srcEvent == null) {
        return ResponseStatus.EVENT_NOT_FOUND;
      }
      if (!usersMap.get(targetUserName).addEvent((Event) srcEvent.clone())) {
        return ResponseStatus.EVENT_ALREADY_EXISTS;
      }
    } catch (CloneNotSupportedException ex) { /* won't happen */ }
    return ResponseStatus.EVENT_CLONED;
  }

  public String getUserInfo(String userName) {
    User user = usersMap.get(userName);
    return (user != null) ? user.toString() : "";
  }

  public void startScheduling() {
    TimerTask timerTask = new TimerTask() {
      @Override
      public void run() {
        long currentTime = new Date().getTime() / 1000;

        for (User user : usersMap.values()) {
          if (user.isActive()) {
            for (Event event : user.getUserTaskArray()) {
              if (event.getEventDate().getTime() / 1000 == currentTime) {
                logger.fine("---EVENT---\r\nUser: " + user.getUserName() + "\r\nEvent info: " + event + "\r\n");
              }
            }
          }
        }
      }
    };

    Timer scheduler = new Timer("scheduler", false);
    scheduler.scheduleAtFixedRate(timerTask, 0, 1000);
  }

  public User getUserByName(String userName) {
    return usersMap.get(userName);
  }

  public int getSocketLocalPort() {
    return srvSocket.getLocalPort();
  }

  public JSONObject getCurrentState() {
    JSONObject result = new JSONObject();
    JSONArray usersArray = new JSONArray();
    for (User user : usersMap.values()) {
      usersArray.add(user.getUserInfoAsJSON());
    }
    result.put("usersList", usersArray);
    return result;
  }

  public boolean parseStateJSON(String input) {
    JSONParser parser = new JSONParser();
    SortedMap<String, User> usersMap = new TreeMap<String, User>();

    try {
      JSONObject parsedObject = (JSONObject) parser.parse(input);
      JSONArray usersArray = (JSONArray) parsedObject.get("usersList");

      Iterator<JSONObject> usersIterator = usersArray.iterator();
      while (usersIterator.hasNext()) {
        JSONObject JSONUser = usersIterator.next();
        String userName = (String) JSONUser.get("userName");
        String timeZoneID = (String) JSONUser.get("timeZoneID");
        boolean isActive = (Boolean) JSONUser.get("isActive");
        User user = new User(userName, TimeZone.getTimeZone(timeZoneID), isActive);

        JSONArray userEventSet = (JSONArray) JSONUser.get("userTaskSet");
        Iterator<JSONObject> eventIterator = userEventSet.iterator();
        while (eventIterator.hasNext()) {
          JSONObject JSONDate = eventIterator.next();
          Date eventDate = new Date((Long) JSONDate.get("eventDate"));
          String eventText = (String) JSONDate.get("eventText");
          Event event = new Event(eventDate, eventText);
          user.addEvent(event);
        }
        usersMap.put(userName, user);
      }
      this.usersMap = usersMap;
      return true;
    } catch (ParseException ex) {
      return false;
    }
  }

  public void startSyncThread() {
    Thread syncThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            Socket in = srvSocket.accept();
            OutputStream outputStream = in.getOutputStream();
            outputStream.write(getCurrentState().toJSONString().getBytes());
            outputStream.flush();
            in.close();
          } catch (Exception ex) {
            ex.printStackTrace();
            logger.warning("SERVER SOCKET THREAD: Ошибка при передаче своего состояния");
          }
        }
      }
    });
    syncThread.start();
  }

  public void sync(String host, int port) {
    StringBuilder sb = new StringBuilder();
    try (Socket s = new Socket()) {
      s.connect(new InetSocketAddress(host, port), 5000);
      Scanner in = new Scanner(s.getInputStream());
      while (in.hasNextLine()) {
        sb.append(in.nextLine());
      }
      logger.info("State recovered successfully");
    } catch (IOException ioex) {
      logger.warning("CLIENT SOCKET THREAD: Ошибка при получении состояния");
      ioex.printStackTrace();
    }
    this.parseStateJSON(sb.toString());
  }

  public boolean saveStateToDB() {
    try {
      dao.saveSchedulerState(usersMap);
      System.out.println("Scheduler state saved successfully");
      return true;
    } catch (SQLException sqlex) {
      System.out.println("Error when saving state to db");
      sqlex.printStackTrace();
      return false;
    }
  }

  public boolean loadStateFromDB() {
    try {
      this.usersMap = dao.loadSchedulerState();
      System.out.println("Scheduler state loaded successfully");
      return true;
    } catch (SQLException sqlex) {
      System.out.println("Error when loading state from db");
      sqlex.printStackTrace();
      return false;
    }
  }
}
