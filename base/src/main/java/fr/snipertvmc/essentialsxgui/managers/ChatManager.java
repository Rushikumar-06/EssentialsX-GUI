package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatManager {


	// -------------------------------------------------- //


	private final Map<UUID, Consumer<String>> playersTyping;
	private final Map<UUID, BukkitTask> playersTypingTasks;


	// -------------------------------------------------- //


	public ChatManager() {
		this.playersTyping = new HashMap<>();
		this.playersTypingTasks = new HashMap<>();
	}


	// -------------------------------------------------- //


	public boolean canDoChat(Player player) {

		if (playersTyping.containsKey(player.getUniqueId())) {
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ONGOING_ACTION, null));
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			return false;
		}

		return true;
	}


	public void addChat(Player player, Consumer<String> consumer, float seconds) {
		playersTyping.put(player.getUniqueId(), consumer);
		if (seconds > 0) {
			long ticks = (long) (seconds * 20);
			playersTypingTasks.put(player.getUniqueId(),
					Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> removeChat(player, false), ticks)
			);
		}
	}


	public void removeChat(Player player, boolean success) {

		if (playersTyping.containsKey(player.getUniqueId()) && !success) {
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.ACTION_EXPIRED, null));
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
		}

		playersTyping.remove(player.getUniqueId());

		playersTypingTasks.get(player.getUniqueId()).cancel();
		playersTypingTasks.remove(player.getUniqueId());
	}


	public boolean isTyping(UUID uuid) {
		return playersTyping.containsKey(uuid);
	}


	public Map<UUID, Consumer<String>> getPlayersTyping() {
		return playersTyping;
	}


	public void accept(Player player, String message) {
		playersTyping.get(player.getUniqueId()).accept(message);
		removeChat(player, true);
	}


	// -------------------------------------------------- //
}
