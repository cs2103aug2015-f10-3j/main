package util;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeCommon {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
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
			return inputDateTime.format(DATE_FORMATTER);
		} catch (DateTimeException e) {
			return null;
		}
	}

	public static String getTime(LocalDateTime inputDateTime) {
		try {
			return inputDateTime.format(TIME_FORMATTER);
		} catch (DateTimeException e) {
			return null;
		}
	}

	public static LocalDateTime now() {
		return LocalDateTime.now();
	}
}
