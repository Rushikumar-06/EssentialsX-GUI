package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.hooks.EssentialsHook;
import fr.snipertvmc.essentialsxgui.hooks.WorldGuardHook;

public class HookManager {


	// -------------------------------------------------- //


	private final EssentialsHook essentialsHook;
	private final WorldGuardHook  worldGuardHook;


	// -------------------------------------------------- //


	public HookManager() {
		essentialsHook = new EssentialsHook();
		worldGuardHook = new WorldGuardHook();
	}


	// -------------------------------------------------- //


	public EssentialsHook getEssentialsHook() {
		return essentialsHook;
	}
	public WorldGuardHook getWorldGuardHook() {
		return worldGuardHook;
	}


	// -------------------------------------------------- //
}
