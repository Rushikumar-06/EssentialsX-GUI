package fr.snipertvmc.essentialsxgui.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class WorldGuardHook {


	public boolean isSupported() {
		try {
			Class.forName("com.sk89q.worldguard.WorldGuard");
			return true;

		} catch (ClassNotFoundException e) {
			return false;
		}
	}


	public List<ProtectedRegion> getPlayerRegions(Player player) {

		if (!isSupported()) return List.of();

		RegionContainer regionContainer =  WorldGuard.getInstance().getPlatform().getRegionContainer();
		World wgWorld = BukkitAdapter.adapt(player.getWorld());
		RegionManager regionManager = regionContainer.get(wgWorld);
		if (regionManager == null) {
			ConsoleLogger.warn("WorldGuard region manager is null for world: " + player.getWorld().getName());
			return null;
		}

		BlockVector3 blockVector3 = BukkitAdapter.asBlockVector(player.getLocation());
		return regionManager.getApplicableRegions(blockVector3).getRegions().stream().toList();
	}


	public boolean canExecuteCommand(Player player, String command) {

		if (!isSupported()) return true;

		List<ProtectedRegion> regions = getPlayerRegions(player);
		if (regions == null || regions.isEmpty()) return true;

		Flag<?> blockedCommandsFlag = WorldGuard.getInstance().getFlagRegistry().get("blocked-cmds");
		if (!command.startsWith("/")) command = "/" + command;

		for (ProtectedRegion region : regions) {
			if (region.getFlags().containsKey(blockedCommandsFlag)) {
				Object regionBlockedCommandsFlag = region.getFlag(blockedCommandsFlag);
				if (regionBlockedCommandsFlag instanceof Set<?> blockedCommands) {
					if (blockedCommands.contains(command)) {
						return false;
					}
				}
			}
		}

		return true;
	}
}
