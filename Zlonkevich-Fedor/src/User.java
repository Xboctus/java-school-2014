
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.TreeMap;


class User implements Runnable{

    String      varName, varActive;
    TimeZone    varTimeZone;

    TreeMap<String, Event> eventsList = new TreeMap<String, Event>();
        
    //конструктор; задает параметры пользователя
    User(String name, String timeZoneID, String active) {
        varName         = name;
        varTimeZone     = getTimeZone(timeZoneID);
        varActive       = active;        
    }
    

    
    //метод изменяет параметры пользователя
    void modifyUser(String name, String timeZoneID, String active){
        varName         = name;
        varTimeZone     = getTimeZone(timeZoneID);
        varActive       = active;
    }
    
    //добавляет событие для пользователя
    void addEvent(String eventName, Date date) {
        eventsList.put(eventName, new Event(eventName, date));
    }
    
    //используется при клонировании события другому пользователю
    void addEvent(String eventName, Event event) {
        eventsList.put(eventName, event);
    }
    
    //создает случайную дату в диапазоне и вызывает addEvent();
    void addRandomTimeEvent(String eventName, Date dateFrom, Date dateTo){
        Random rand = new Random();
        long randVal = rand.nextInt((int)(dateTo.getTime() - dateFrom.getTime())) + dateFrom.getTime();
        addEvent(eventName, new Date(randVal));
    }
        
    //метод удаляет объект из списка событий пользователя
    void removeEvent(String text){
        eventsList.remove(text);
    }
    
    //возвращает событие указанного пользователя
    Event cloneEvent(String eventName){
         Event cloned = eventsList.get(eventName);
         return cloned;
    }
    
    //введенное в команде значение "GMT+X" сравнивается со списком существующих
    //таймзон, возвращает ID этой таймзоны
    TimeZone getTimeZone(String timeZoneID) {
        
        TimeZone    tZone   = new TimeZone() {

            @Override
            public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void setRawOffset(int offsetMillis) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public int getRawOffset() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean useDaylightTime() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public boolean inDaylightTime(Date date) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        String      id      = null;

        for (String t : tZone.getAvailableIDs()) { // проверяет существует ли эта зона
            if (t.equals("Etc/" + timeZoneID)) {
                id = t;
                System.out.println(t);                
                break;
            } else {
//                System.out.println("There is no such TimeZone!");                
            }
        }
        tZone  = new SimpleTimeZone(0, id);        

        return tZone;
    }

    
    void showEvents(){
        Collection<Event> c = eventsList.values();
        Iterator itr = c.iterator();
        while(itr.hasNext()){
            ((Event)itr.next()).show();
        }        
    }
    
    boolean isActive(){
        return varActive.equalsIgnoreCase("Active");
    }

    void startUserThread(){
        Thread t = new Thread(this, varName);
        t.start();
        
    }    
    
    @Override
    public void run(){
        try{
            Collection<Event> c = eventsList.values();
        
        Iterator itr = c.iterator();
        while(itr.hasNext()){
            if(((Event)itr.next()).date.equals(new Date())){
                ((Event)itr.next()).show();
            }
            Thread.sleep(1000);
            run();
        }
                
        }catch(InterruptedException e){
            System.out.println(varName + "прерван");
        }        
    }
}