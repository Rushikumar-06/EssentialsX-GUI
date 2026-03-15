package fr.snipertvmc.essentialsxgui.utilities.other;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundsUtils {


	// -------------------------------------------------- //


	public static void playSound(Player player, EXGSound sound) {
		playEXGSound(player, sound, 1, 1, false);
	}

	public static void playSound(Player player, EXGSound sound, boolean ignoreDisabled) {
		playEXGSound(player, sound, 1, 1, ignoreDisabled);
	}

	public static void playSound(Player player, EXGSound sound, float volume) {
		playEXGSound(player, sound, volume, 1, false);
	}

	public static void playSound(Player player, EXGSound sound, float volume, float pitch) {
		playEXGSound(player, sound, volume, pitch, false);
	}

	public static void playSound(Player player, EXGSound sound, float volume, float pitch, boolean ignoreDisabled) {
		playEXGSound(player, sound, volume, pitch, ignoreDisabled);
	}


	// -------------------------------------------------- //


	private static void playEXGSound(Player player, EXGSound sound, float volume, float pitch, boolean ignoreDisabled) {

		if (!ignoreDisabled && !Main.getInstance().getConfiguration().areSoundsEnabled()) {
			return;
		}

		if (player == null || !player.isOnline()) {
			return;
		}

		if (sound.getSound() == null) {
			return;
		}

		if (volume < 0 || volume > 1) {
			volume = 1;
		}

		if (pitch < 0 || pitch > 2) {
			pitch = 1;
		}

		player.playSound(player.getLocation(), sound.getSound(), volume, pitch);
	}


	// -------------------------------------------------- //
}
