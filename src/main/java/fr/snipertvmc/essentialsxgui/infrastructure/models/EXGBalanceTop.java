package fr.snipertvmc.essentialsxgui.infrastructure.models;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class EXGBalanceTop {


	// -------------------------------------------------- //


	private long lastUpdate = System.currentTimeMillis();

	private List<Pair<String, Double>> balanceTopEntries = new ArrayList<>();

	private BukkitTask updateTask;


	// -------------------------------------------------- //


	public void forceUpdate() {
		balanceTopEntries = new ArrayList<>();
		Main.getInstance().getEssentials().getBalanceTop().getBalanceTopCache().values().forEach(entry -> {
			balanceTopEntries.add(new Pair<>(entry.getDisplayName(), entry.getBalance().doubleValue()));
		});

		lastUpdate = System.currentTimeMillis();
	}


	public int getPlayerRank(String playerName) {
		return balanceTopEntries.indexOf(
				balanceTopEntries.stream()
						.filter(entry -> entry.getLeft().equals(playerName))
						.findFirst()
						.orElse(null)
		) + 1;
	}


	// -------------------------------------------------- //


	public void startUpdateTask() {
		long updateInterval = Main.getInstance().getConfiguration().getBalanceTopUpdateInterval() * 20L;
		updateTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), this::forceUpdate, 0, updateInterval);
	}


	public void stopUpdateTask() {
		if (updateTask != null) {
			updateTask.cancel();
			updateTask = null;
		}
	}


	public boolean isUpdateTaskRunning() {
		return updateTask != null && !updateTask.isCancelled();
	}


	// -------------------------------------------------- //


	public long getLastUpdate() {
		return lastUpdate;
	}

	public List<Pair<String, Double>> getBalanceTopEntries() {
		return balanceTopEntries;
	}


	// -------------------------------------------------- //
}
