package org.kouzma.schedule.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
/**
 * @author Anastasya Kouzma
 */
public class DateUtil {
	private static int offset = (new GregorianCalendar()).get(Calendar.ZONE_OFFSET);

	public static Date computeRandomDate(Date fromDate, Date toDate) {
		long diff = (toDate.getTime() - fromDate.getTime()) / 1000; // в секундах
		if (diff <= 0)
			return null;
				
		double coefficient = new Random().nextDouble();
		long randomTime = (long) (fromDate.getTime() + diff * coefficient * 1000);
		
		return new Date(randomTime);
	}
	
	public static Date fromGTM(Date date) {
		return new Date(date.getTime() + offset);
	}

	public static Date toGMT(Date date) {
		return new Date(date.getTime() - offset);
	}
	
}
