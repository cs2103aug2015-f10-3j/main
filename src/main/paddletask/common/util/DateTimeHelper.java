//@@author A0125473H
package main.paddletask.common.util;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeHelper {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	private static final String TIME_PADDING = " 00:00";
	
	/*** Methods ***/
	// parses string exclusively in this format "dd/mm/yyyy HH:mm"
	public static LocalDateTime parseStringToDateTime(String inputString) {
		try {
			return LocalDateTime.parse(inputString,DATE_TIME_FORMATTER);
		} catch (DateTimeException e) {
			return null;
		}
	}

	// parses date exclusively in this format "dd/mm/yyyy HH:mm"
	public static String parseDateTimeToString(LocalDateTime inputDateTime) {
		try {
			return inputDateTime.format(DATE_TIME_FORMATTER);
		} catch (DateTimeException e) {
			return null;
		}
	}
	
	public static LocalDateTime setTimezoneForDate(Date date) {
	    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	// Support for retrieval of date and time instead of whole date
	public static String getDate(LocalDateTime inputDateTime) {
		try {
			return inputDateTime.toLocalDate().format(DATE_FORMATTER);
		} catch (DateTimeException e) {
			return null;
		}
	}

	public static String getTime(LocalDateTime inputDateTime) {
		try {
			return inputDateTime.toLocalTime().format(TIME_FORMATTER);
		} catch (DateTimeException e) {
			return null;
		}
	}
	
	public static boolean isLater(LocalDateTime baseline, LocalDateTime than) {
		return baseline.compareTo(than) > 0;
	}
	
	public static boolean isEqual(LocalDateTime baseline, LocalDateTime than) {
	    return baseline.compareTo(than) == 0;
	}
	
	public static String getDayOfWeek(String inputDate) {
		try {
			LocalDateTime date = parseStringToDateTime(inputDate + TIME_PADDING);
			if (date != null) {
				return date.getDayOfWeek().toString();
			}
		} catch (DateTimeException e) {}
		return inputDate;
	}
	
	public static boolean isDate(String date) {
		return date.matches("(\\d{2})/(\\d{2})/(\\d{4})");
	}

	// Date/Time manipulation helper functions
	public static LocalDateTime now() {
		return LocalDateTime.now();
	}

	public static LocalDateTime addYears(LocalDateTime inputDateTime, int yearsToAdd) {
		return inputDateTime.plusYears(yearsToAdd);
	}

	public static LocalDateTime addMonths(LocalDateTime inputDateTime, int monthsToAdd) {
		return inputDateTime.plusMonths(monthsToAdd);
	}
	
	public static LocalDateTime addWeeks(LocalDateTime inputDateTime, int weeksToAdd) {
        return inputDateTime.plusWeeks(weeksToAdd);
    }

	public static LocalDateTime addDays(LocalDateTime inputDateTime, int daysToAdd) {
		return inputDateTime.plusDays(daysToAdd);
	}

	public static LocalDateTime addHours(LocalDateTime inputDateTime, int hoursToAdd) {
		return inputDateTime.plusHours(hoursToAdd);
	}

	public static LocalDateTime addMinutes(LocalDateTime inputDateTime, int minutesToAdd) {
		return inputDateTime.plusMinutes(minutesToAdd);
	}
}
