package fr.snipertvmc.essentialsxgui.utilities;

import com.earth2me.essentials.libs.kyori.adventure.text.Component;
import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.MiniMessage;
import com.earth2me.essentials.libs.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import com.earth2me.essentials.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TextUtils {


	// -------------------------------------------------- //


	public static void sendMessageToCommandSender(CommandSender commandSender, String formattedMessage) {

		if (Main.getInstance().getLoadingManager().isPlaceholderAPISupported() && commandSender instanceof Player player) {
			formattedMessage = PlaceholderAPI.setPlaceholders(player, formattedMessage);
		}

		commandSender.sendMessage(convertFormattedMessageToText(formattedMessage));
	}


	// -------------------------------------------------- //


	public static String convertFormattedMessageToText(String formattedMessage) {
		return convertComponentToText(getComponent(formattedMessage));
	}


	public static List<String> convertFormattedMessagesToText(List<String> formattedMessages) {
		return formattedMessages.stream().map(TextUtils::convertFormattedMessageToText).toList();
	}


	public static String convertComponentToText(Component component) {
		if (MCServerVersion.getMCServerVersion().isHigherThan(MCServerVersion.v1_15_2)) {
			return TextComponent.toLegacyText(BungeeComponentSerializer.get().serialize(component));

		} else {
			return LegacyComponentSerializer.legacySection().serialize(component);
		}
	}


	// -------------------------------------------------- //


	private static Component getComponent(String formattedMessage) {

		if (hasMixedFormat(formattedMessage)) {
			ConsoleLogger.warn("Mixed formatting detected in message: " + formattedMessage);
			ConsoleLogger.warn("Please use either legacy or MiniMessage formatting, not both. "
					+ "This message will be treated as a plain text message.");

			return Component.text(formattedMessage);
		}

		if (hasLegacyFormat(formattedMessage)) {
			formattedMessage = formattedMessage.replace("&", "§");
			return LegacyComponentSerializer.legacySection().deserialize(formattedMessage);
		}

		return MiniMessage.miniMessage().deserialize(formattedMessage);
	}


	// -------------------------------------------------- //


	public static boolean hasLegacyFormat(String message) {
		if (message == null || message.isEmpty()) return false;
		return message.matches(".*[&§][0-9a-fk-orx].*");
	}


	public static boolean hasMiniMessageFormat(String message) {
		if (message == null || message.isEmpty()) return false;
		return message.matches(".*<[a-z0-9_#/:-]+>.*");
	}


	public static boolean hasMixedFormat(String message) {
		if (message == null || message.isEmpty()) return false;
		return hasLegacyFormat(message) && hasMiniMessageFormat(message);
	}


	// -------------------------------------------------- //
}
