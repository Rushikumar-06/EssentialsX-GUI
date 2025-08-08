package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
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


	public boolean canDoChat(UUID uuid) {

		if (playersTyping.containsKey(uuid)) {
			Player player = Main.getInstance().getPlayerManager().getPlayer(uuid.toString()).getPlayer();
			player.sendMessage(MessagesUtils.get(EXGMessage.ONGOING_ACTION, null));
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			return false;
		}

		return true;
	}


	public void addChat(UUID uuid, Consumer<String> consumer, float seconds) {
		playersTyping.put(uuid, consumer);
		if (seconds > 0) {
			long ticks = (long) (seconds * 20);
			playersTypingTasks.put(uuid,
					Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> removeChat(uuid, false), ticks)
			);
		}
	}


	public void removeChat(UUID uuid, boolean success) {

		if (playersTyping.containsKey(uuid) && !success) {
			Player player = Main.getInstance().getPlayerManager().getPlayer(uuid.toString()).getPlayer();
			player.sendMessage(MessagesUtils.get(EXGMessage.ACTION_EXPIRED, null));
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
		}

		playersTyping.remove(uuid);

		playersTypingTasks.get(uuid).cancel();
		playersTypingTasks.remove(uuid);
	}


	public boolean isTyping(UUID uuid) {
		return playersTyping.containsKey(uuid);
	}


	public Map<UUID, Consumer<String>> getPlayersTyping() {
		return playersTyping;
	}


	public void accept(UUID uuid, String message) {
		playersTyping.get(uuid).accept(message);
		removeChat(uuid, true);
	}


	// -------------------------------------------------- //
}
