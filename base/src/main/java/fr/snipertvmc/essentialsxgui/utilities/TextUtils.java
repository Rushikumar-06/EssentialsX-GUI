package fr.snipertvmc.essentialsxgui.utilities;

import fr.snipertvmc.essentialsxgui.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class TextUtils {


	// -------------------------------------------------- //


	public static void sendMessageToCommandSender(CommandSender commandSender, String formattedMessage) {
		if (commandSender == null || formattedMessage == null || formattedMessage.isEmpty()) return;
		sendMessageToCommandSender(Set.of(commandSender), formattedMessage);
	}


	public static void sendMessageToCommandSender(Set<CommandSender> commandSenders, String formattedMessage) {
		if (formattedMessage == null || formattedMessage.isEmpty() || commandSenders.isEmpty()) return;

		CommandSender firstSender = commandSenders.stream().findFirst().orElse(null);
		Component component = convertFormattedMessageToComponent(firstSender, formattedMessage);

		for (CommandSender sender : commandSenders) {
			getAudience(sender).sendMessage(component);
		}
	}


	public static String convertFormattedMessageToText(String formattedMessage) {
		if (formattedMessage == null || formattedMessage.isEmpty()) return "";
		Component component = convertFormattedMessageToComponent(null, formattedMessage);
		return LegacyComponentSerializer.legacySection().serialize(component);
	}


	public static List<String> convertFormattedMessagesToText(List<String> formattedMessages) {
		if (formattedMessages == null) return List.of();
		return formattedMessages.stream().map(TextUtils::convertFormattedMessageToText).toList();
	}


	// -------------------------------------------------- //


	private static Component convertFormattedMessageToComponent(CommandSender commandSender, String formattedMessage) {
		if (Main.getInstance().getHookManager().getPlaceholderAPIHook().isSupported() && commandSender instanceof Player player) {
			formattedMessage = PlaceholderAPI.setPlaceholders(player, formattedMessage);
		}

		if (hasLegacyFormat(formattedMessage)) {
			String sectionMessage = replaceAmpersand(formattedMessage);
			return LegacyComponentSerializer.legacySection().deserialize(sectionMessage);
		} else {
			return MiniMessage.miniMessage().deserialize(formattedMessage);
		}
	}


	// -------------------------------------------------- //


	private static Audience getAudience(CommandSender sender) {
		if (Main.getInstance().getLibraryManager().hasNativeAdventureSupport()) {
			return (Audience) sender;
		} else {
			return Main.getInstance().getBukkitAudiences().sender(sender);
		}
	}


	// -------------------------------------------------- //


	public static boolean hasLegacyFormat(String message) {
		if (message == null || message.isEmpty()) return false;
		return message.matches(".*[&§][0-9a-fk-orx].*");
	}


	public static String replaceAmpersand(String message) {
		if (message == null) return "";
		return message.replaceAll("(?i)&([0-9a-fk-orx])", "§$1");
	}


	// -------------------------------------------------- //


	public static String firstLetterToUpperCase(String string) {
		if (string == null || string.isEmpty()) return "";
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}


	// -------------------------------------------------- //
}
