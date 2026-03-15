package fr.snipertvmc.essentialsxgui.utilities;

import com.earth2me.essentials.libs.kyori.adventure.text.Component;
import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.MiniMessage;
import com.earth2me.essentials.libs.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import fr.snipertvmc.essentialsxgui.Main;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TextUtils {


	// -------------------------------------------------- //


	public static void sendComponentToCommandSender(CommandSender commandSender, Component component) {
		Main.getInstance().getBukkitAudiences().sender(commandSender).sendMessage(component);
	}


	// -------------------------------------------------- //


	public static Component convertMiniMessageToComponent(String miniMessage) {
		return MiniMessage.miniMessage().deserialize(miniMessage);
	}


	public static String convertMiniMessageToText(String miniMessage) {
		return TextComponent.toLegacyText(BungeeComponentSerializer.get().serialize(convertMiniMessageToComponent(miniMessage)));
	}


	public static List<String> convertMiniMessagesToText(List<String> miniMessages) {
		return miniMessages.stream().map(TextUtils::convertMiniMessageToText).toList();
	}


	// -------------------------------------------------- //
}
