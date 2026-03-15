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


	public static String formatDateDiffZoned(long targetMillis) {
		ZoneId zone = Main.getInstance().getConfiguration().getDateTimezone();
		ZonedDateTime now = ZonedDateTime.now(zone);
		ZonedDateTime target = Instant.ofEpochMilli(targetMillis).atZone(zone);

		long seconds = Math.abs(Duration.between(now, target).getSeconds());

		long days = seconds / 86400;
		seconds %= 86400;

		long hours = seconds / 3600;
		seconds %= 3600;

		long minutes = seconds / 60;
		seconds %= 60;

		StringBuilder result = new StringBuilder();

		if (days > 0) {
			result.append(MessagesUtils.getComponent(
					EXGMessage.DAYS,
					Map.of("days", String.valueOf(days))
			)).append(" ");
		}

		if (hours > 0) {
			result.append(MessagesUtils.getComponent(
					EXGMessage.HOURS,
					Map.of("hours", String.valueOf(hours))
			)).append(" ");
		}

		if (minutes > 0) {
			result.append(MessagesUtils.getComponent(
					EXGMessage.MINUTES,
					Map.of("minutes", String.valueOf(minutes))
			)).append(" ");
		}

		if (seconds > 0 || result.isEmpty()) {
			result.append(MessagesUtils.getComponent(
					EXGMessage.SECONDS,
					Map.of("seconds", String.valueOf(seconds))
			));
		}

		return result.toString().trim();
	}



	// -------------------------------------------------- //
}
