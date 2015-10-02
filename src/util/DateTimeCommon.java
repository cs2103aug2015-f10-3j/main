package util;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeCommon {
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public static LocalDateTime parseStringToDateTime(String inputString) {
		return LocalDateTime.parse(inputString,DATE_FORMATTER);
	}
	
	public static String parseDateTimeToString(LocalDateTime inputDateTime) {
		return inputDateTime.format(DATE_FORMATTER);
	}
}
