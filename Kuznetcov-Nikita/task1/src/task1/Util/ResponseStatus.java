package task1.Util;

/**
 * Created by Sunrise on 04.07.2014.
 * Enum contains all the Coordinator response types.
 */
public enum ResponseStatus {
  // errors with anything that binded with users list
  USER_ALREADY_EXISTS,
  USER_NOT_FOUND,
  TARGET_USER_NOT_FOUND,

  // errors with formats and difference between dates
  BAD_TIMEZONE_FORMAT,
  BAD_DATE_FORMAT,
  WRONG_DATE_DIFFERENCE,

  // error with events
  EVENT_NOT_FOUND,
  EVENT_ALREADY_EXISTS,

  // successful status
  USER_ADDED,
  USER_MODIFIED,
  EVENT_ADDED,
  EVENT_REMOVED,
  EVENT_CLONED,

}
