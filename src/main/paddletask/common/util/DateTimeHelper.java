package main.paddletask.common.util;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeHelper {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter AMERICAN_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	
	public static LocalDateTime parseStringToDateTime(String inputString) {
		try {
			return LocalDateTime.parse(inputString,DATE_TIME_FORMATTER);
		} catch (DateTimeException e) {
			return null;
		}
	}

	public static String parseDateTimeToString(LocalDateTime inputDateTime) {
		try {
			return inputDateTime.format(DATE_TIME_FORMATTER);
		} catch (DateTimeException e) {
			return null;
		}
	}

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