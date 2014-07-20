
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.TreeMap;


public class Coordinator{
    
    TreeMap<String, User> usersList = new TreeMap<String, User>();

    String cmdCreate                = "Create", 
           cmdAddEvent              = "AddEvent", 
           cmdAddRandomTimeEvent    = "AddRandomTimeEvent", 
           cmdStartScheduling       = "StartScheduling", 
           cmdRemoveEvent           = "RemoveEvent", 
           cmdCloneEvent            = "CloneEvent",
           cmdModify                = "Modify",
           cmdShowInfo              = "ShowInfo",
           varActive, varName, varText, varNameTo;
    String timeZoneID;
    Date varDateTime, varDateFrom, varDateTo;
    
    //вызывается, когда программа запущена в режиме показа и выводит расписание
    //активных пользователей
    void showSchedule() {
    }
    
    //преобразует строку в дату
    Date parseDate(String target) throws ParseException{
        DateFormat df = new SimpleDateFormat("dd.mm.yyyy-kk:mm:ss");
        Date date = df.parse(target);
        return date;
    }

    //метод считывает и обрабатывает команды из командной строки
    void readCommands() {
        String input;
        Scanner sc = new Scanner(System.in);
        input = sc.nextLine();
        System.out.println(input.length());
        
//распознает команды и создает юзера
        if (input.substring(0, cmdCreate.length()).equals(cmdCreate)) {
            String[] st = input.substring(cmdCreate.length() + 1, input.length() - 1).split(", ");
            varName     = st[0];
            timeZoneID  = st[1];
            varActive   = st[2];
            usersList.put(varName, new User(varName, timeZoneID, varActive));
            readCommands();

//распознает команду и меняет параметры юзера    
        } else if (input.substring(0, cmdModify.length()).equals(cmdModify)) {
            String[] st = input.substring(cmdModify.length() + 1, input.length() - 1).split(", ");
            varName     = st[0];
            timeZoneID  = st[1];
            varActive   = st[2];
            usersList.get(varName).modifyUser(varName, timeZoneID, varActive);             
            readCommands();
            
//распознает команду и добавляет новое событие            
        } else if (input.substring(0, cmdAddEvent.length()).equals(cmdAddEvent)) {
            String[] st = input.substring(cmdAddEvent.length() + 1, input.length() - 1).split(", ");
            varName     = st[0];
            varText     = st[1];
            try {
                varDateTime = parseDate(st[2]);
            } catch (ParseException ex) {
                System.out.println("Wrong date format!");
                readCommands();
            }
            usersList.get(varName).addEvent(varText, varDateTime);
            readCommands();            
            
//распознает команду и удаляет событие            
        } else if (input.substring(0, cmdRemoveEvent.length()).equals(cmdRemoveEvent)) {
            String[] st = input.substring(cmdRemoveEvent.length() + 1, input.length() - 1).split(", ");
            varName     = st[0];
            varText     = st[1];
            usersList.get(varName).removeEvent(varText);
            readCommands();
            
//распознает команду и добавляет рандомное событие            
        } else if (input.substring(0, cmdShowInfo.length()).equals(cmdShowInfo)){
            String[] str = input.substring(cmdShowInfo.length() + 1, input.length() - 1).split(", ");           
            varName     = str[0];
            usersList.get(varName).showEvents();
            
            
        }  else if (input.substring(0, cmdAddRandomTimeEvent.length()).equals(cmdAddRandomTimeEvent)) {
            String[] st = input.substring(cmdAddRandomTimeEvent.length() + 1, input.length() - 1).split(", ");
            varName     = st[0];
            varText     = st[1];
            try {
                varDateFrom = parseDate(st[2]);
                varDateTo   = parseDate(st[3]);
            } catch (ParseException ex) {
                System.out.println("Wrong date format!");
                readCommands();
            }
            usersList.get(varName).addRandomTimeEvent(varText, varDateFrom, varDateTo);
            readCommands();

//распознает команду и клонирует событие            
        } else if (input.substring(0, cmdCloneEvent.length()).equals(cmdCloneEvent)){
            String[] str = input.substring(cmdCloneEvent.length() + 1, input.length() - 1).split(", ");           
            varName     = str[0];
            varText     = str[1];
            varNameTo   = str[2];
            usersList.get(varNameTo).addEvent(varText, usersList.get(varName).cloneEvent(varText));
            readCommands();
            
        }else if (input.substring(0, cmdStartScheduling.length()).equals(cmdStartScheduling)){
            System.out.println(input);
            Collection<User> c = usersList.values();
            Iterator itr = c.iterator();
            while(itr.hasNext()){
                if(((User)itr.next()).varActive.equals("active")){
                    ((User)itr.next()).startUserThread();
                }
            }
        }else{
            System.out.println("Something wrong!");
            readCommands();
        }

    }
    
        
//    @Override
//    public void run(){
//        Thread t;
//        
//    }

    //обощенный метод для проверки совпадения имен юзеров в списке юзеров 
    //и событий в списке событий  
    /*
    static <T, V extends T> boolean isIn(T x, V[] y) {
        for (int i = 0; i < y.length; i++) {
            if (x.equals(y[i])) {
                return true;
            }
        }
        return false;
    }
    */
    
    public static void main(String[] args) {
        Coordinator schedule = new Coordinator();
        schedule.readCommands();        
    }
}

// Create(User, GMT+0, Active)
// AddEvent(User, dinner, 11.07.2014-18:52:00)
// StartScheduling
// ShowInfo(User)
