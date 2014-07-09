package task1.DAO;

import task1.User;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Sunrise on 08.07.2014.
 */
public interface ISchedulerDAO {

  void saveSchedulerState(Map<String, User> usersMap) throws SQLException;
  ConcurrentSkipListMap<String, User> loadSchedulerState() throws SQLException;
  void closeConnection() throws SQLException;

}
