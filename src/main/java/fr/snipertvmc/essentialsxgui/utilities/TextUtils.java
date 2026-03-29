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


	public static void sendComponentToCommandSender(CommandSender commandSender, Component component) {

		String miniMessage = convertComponentToMiniMessage(component);

		if (Main.getInstance().getLoadingManager().isPlaceholderAPISupported() && commandSender instanceof Player player) {
			miniMessage = PlaceholderAPI.setPlaceholders(player, miniMessage);
		}

		commandSender.sendMessage(convertMiniMessageToText(miniMessage));
	}


	// -------------------------------------------------- //


	public static Component convertMiniMessageToComponent(String miniMessage) {
		return MiniMessage.miniMessage().deserialize(miniMessage);
	}


	public static String convertComponentToMiniMessage(Component component) {
		return MiniMessage.miniMessage().serialize(component);
	}


	public static String convertMiniMessageToText(String miniMessage) {
		if (MCServerVersion.getMCServerVersion().isHigherThan(MCServerVersion.v1_15_2)) {
			return TextComponent.toLegacyText(BungeeComponentSerializer.get().serialize(convertMiniMessageToComponent(miniMessage)));

		} else {
			return LegacyComponentSerializer.legacySection().serialize(convertMiniMessageToComponent(miniMessage));
		}
	}


	public static List<String> convertMiniMessagesToText(List<String> miniMessages) {
		return miniMessages.stream().map(TextUtils::convertMiniMessageToText).toList();
	}


	// -------------------------------------------------- //
}
