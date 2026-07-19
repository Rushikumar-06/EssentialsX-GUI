package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.hooks.EssentialsHook;
import fr.snipertvmc.essentialsxgui.hooks.PlaceholderAPIHook;
import fr.snipertvmc.essentialsxgui.hooks.WorldGuardHook;

public class HookManager {


	// -------------------------------------------------- //


	private final EssentialsHook essentialsHook;
	private final PlaceholderAPIHook placeholderAPIHook;
	private final WorldGuardHook  worldGuardHook;


	// -------------------------------------------------- //


	public HookManager() {
		essentialsHook = new EssentialsHook();
		placeholderAPIHook = new PlaceholderAPIHook();
		worldGuardHook = new WorldGuardHook();
	}


	// -------------------------------------------------- //


	public EssentialsHook getEssentialsHook() {
		return essentialsHook;
	}
	public PlaceholderAPIHook getPlaceholderAPIHook() {
		return placeholderAPIHook;
	}
	public WorldGuardHook getWorldGuardHook() {
		return worldGuardHook;
	}


	// -------------------------------------------------- //
}
