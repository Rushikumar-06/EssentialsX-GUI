package fr.snipertvmc.essentialsxgui.utilities;

import com.earth2me.essentials.libs.kyori.adventure.text.Component;
import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.MiniMessage;
import com.earth2me.essentials.libs.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import com.earth2me.essentials.libs.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import com.earth2me.essentials.libs.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TextUtils {


	// -------------------------------------------------- //


	public static void sendComponentToCommandSender(CommandSender commandSender, Component component) {
		Main.getInstance().getBukkitAudiences().sender(commandSender).sendMessage(component);
	}


	// -------------------------------------------------- //


	public static boolean isValidMiniMessage(String miniMessage) {

		try {
			convertMiniMessageToComponent(miniMessage);

		} catch (ParsingExceptionImpl e) {
			return false;
		}

		return true;
	}


	public static Component convertMiniMessageToComponent(String miniMessage) {
		return MiniMessage.miniMessage().deserialize(miniMessage);
	}


	public static String convertMiniMessageToText(String miniMessage) {
		if (MCServerVersion.getMCServerVersion().isLowerThan(MCServerVersion.v1_16)) {
			return LegacyComponentSerializer.legacySection().serialize(convertMiniMessageToComponent(miniMessage));

		} else {
			return TextComponent.toLegacyText(BungeeComponentSerializer.get().serialize(convertMiniMessageToComponent(miniMessage)));
		}
	}


	public static List<String> convertMiniMessagesToText(List<String> miniMessages) {
		return miniMessages.stream().map(TextUtils::convertMiniMessageToText).toList();
	}


	// -------------------------------------------------- //
}
