package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;

import java.util.*;
import java.util.function.Consumer;

public class ChatManager {


	// -------------------------------------------------- //


	private Map<UUID, Consumer<String>> playersTyping;


	// -------------------------------------------------- //


	public ChatManager() {
		this.playersTyping = new HashMap<>();
	}


	// -------------------------------------------------- //


	public void addChat(UUID uuid, Consumer<String> consumer, float seconds) {
		playersTyping.put(uuid, consumer);
		if (seconds > 0) {
			long milliseconds = (long) (seconds * 1000);
			Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), () -> removeChat(uuid), milliseconds);
		}
	}


	public void removeChat(UUID uuid) {
		playersTyping.remove(uuid);
	}


	public boolean isTyping(UUID uuid) {
		return playersTyping.containsKey(uuid);
	}


	public Map<UUID, Consumer<String>> getPlayersTyping() {
		return playersTyping;
	}


	public void accept(UUID uuid, String message) {
		playersTyping.get(uuid).accept(message);
		removeChat(uuid);
	}


	// -------------------------------------------------- //
}
