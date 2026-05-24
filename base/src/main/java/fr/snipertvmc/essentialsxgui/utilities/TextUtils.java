package fr.snipertvmc.essentialsxgui.utilities;

import fr.snipertvmc.essentialsxgui.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class TextUtils {


	// -------------------------------------------------- //


	public static void sendMessageToCommandSender(CommandSender commandSender, String formattedMessage) {
		sendMessageToCommandSender(Set.of(commandSender), formattedMessage);
	}


	public static void sendMessageToCommandSender(Set<CommandSender> commandSenders, String formattedMessage) {
		if (formattedMessage.isEmpty()) return;
		if (commandSenders.isEmpty()) return;
		Component component = convertFormattedMessageToComponent(commandSenders.stream().findFirst().orElse(null), formattedMessage);
		commandSenders.forEach(commandSender -> Main.getInstance().getBukkitAudiences().sender(commandSender).sendMessage(component));
	}


	public static String convertFormattedMessageToText(String formattedMessage) {
		Component component = convertFormattedMessageToComponent(null, formattedMessage);
		return TextComponent.toLegacyText(BungeeComponentSerializer.get().serialize(component));
	}


	public static List<String> convertFormattedMessagesToText(List<String> formattedMessages) {
		return formattedMessages.stream().map(TextUtils::convertFormattedMessageToText).toList();
	}


	// -------------------------------------------------- //


	private static Component convertFormattedMessageToComponent(CommandSender commandSender, String formattedMessage) {

		if (Main.getInstance().getLoadingManager().isPlaceholderAPISupported() && commandSender instanceof Player player) {
			formattedMessage = PlaceholderAPI.setPlaceholders(player, formattedMessage);
		}

		Component component;
		formattedMessage = replaceAmpersand(formattedMessage);

		if (hasLegacyFormat(formattedMessage)) {
			component = LegacyComponentSerializer.legacySection().deserialize(formattedMessage);
		} else {
			component = MiniMessage.miniMessage().deserialize(formattedMessage);
		}

		return component;
	}


	// -------------------------------------------------- //


	public static boolean hasLegacyFormat(String message) {
		if (message == null || message.isEmpty()) return false;
		return message.matches(".*[&§][0-9a-fk-orx].*");
	}


	public static String replaceAmpersand(String message) {
		return message.replaceAll("(?i)&([0-9a-fk-or])", "§$1");
	}


	// -------------------------------------------------- //


	public static String firstLetterToUpperCase(String string) {
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}


	// -------------------------------------------------- //
}
