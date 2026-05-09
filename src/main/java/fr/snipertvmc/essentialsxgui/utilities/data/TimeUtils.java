package fr.snipertvmc.essentialsxgui.utilities.data;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

public class TimeUtils {


	// -------------------------------------------------- //


	public static String formatSeconds(int seconds) {
		return MessagesUtils.getString(EXGMessage.SECONDS, Map.of("seconds", String.valueOf(seconds)));
	}

	public static String formatMinutes(int minutes) {
		return MessagesUtils.getString(EXGMessage.MINUTES, Map.of("minutes", String.valueOf(minutes)));
	}

	public static String formatHours(int hours) {
		return MessagesUtils.getString(EXGMessage.HOURS, Map.of("hours", String.valueOf(hours)));
	}

	public static String formatDays(int days) {
		return MessagesUtils.getString(EXGMessage.DAYS, Map.of("days", String.valueOf(days)));
	}


	// -------------------------------------------------- //


	public static String formatAgoTime(int totalSeconds) {
		String duration = formatDuration(totalSeconds);
		return MessagesUtils.getString(EXGMessage.AGO_TIME_FORMAT, Map.of("time", duration));
	}


	public static String formatInTime(int totalSeconds) {
		String duration = formatDuration(totalSeconds);
		return MessagesUtils.getString(EXGMessage.IN_TIME_FORMAT, Map.of("time", duration));
	}


	public static String formatSinceTime(int totalSeconds) {
		String duration = formatDuration(totalSeconds);
		return MessagesUtils.getString(EXGMessage.SINCE_TIME_FORMAT, Map.of("time", duration));
	}


	// -------------------------------------------------- //


	public static String formatDateDiffZoned(long targetMillis) {
		ZoneId zone = Main.getInstance().getConfiguration().getDateTimezone();
		ZonedDateTime now = ZonedDateTime.now(zone);
		ZonedDateTime target = Instant.ofEpochMilli(targetMillis).atZone(zone);
		long seconds = Math.abs(Duration.between(now, target).getSeconds());
		return formatDuration((int) seconds);
	}


	// -------------------------------------------------- //


	public static String formatDuration(int totalSeconds) {
		int days = totalSeconds / 86400;
		int hours = (totalSeconds % 86400) / 3600;
		int minutes = (totalSeconds % 3600) / 60;
		int seconds = totalSeconds % 60;

		StringBuilder result = new StringBuilder();
		if (days > 0) result.append(formatDays(days)).append(" ");
		if (hours > 0) result.append(formatHours(hours)).append(" ");
		if (minutes > 0) result.append(formatMinutes(minutes)).append(" ");
		if (seconds > 0 || result.isEmpty()) result.append(formatSeconds(seconds));
		return result.toString();
	}


	// -------------------------------------------------- //
}
