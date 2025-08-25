package fr.snipertvmc.essentialsxgui.infrastructure.enums;

import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public enum EXGEntryResult {


	// -------------------------------------------------- //


	// GENERAL
	SUCCESS(null, EXGSound.ACTION_SUCCESS),
	CANCELED(EXGMessage.ACTION_CANCELED, EXGSound.ACTION_CANCELED),


	// STRING TYPES
	LENGTH_LIMIT(EXGMessage.LENGTH_LIMIT, EXGSound.ACTION_FAILURE),
//	INVALID_CHARACTER(EXGMessage.INVALID_CHARACTER, EXGSound.ACTION_FAILURE),
	INVALID_NUMBER(EXGMessage.INVALID_NUMBER, EXGSound.ACTION_FAILURE),


	// MATERIAL TYPES
	INVALID_MATERIAL(EXGMessage.INVALID_MATERIAL, EXGSound.ACTION_FAILURE),;


	// -------------------------------------------------- //


	private final EXGMessage resultMessage;
	private Map<String, String> messageVariables;

	private final EXGSound resultSound;


	// -------------------------------------------------- //


	EXGEntryResult(EXGMessage resultMessage, EXGSound resultSound) {
		this.resultMessage = resultMessage;

		this.resultSound = resultSound;
	}


	// -------------------------------------------------- //


	public EXGEntryResult setMessageVariables(Map<String, String> messageVariables) {
		this.messageVariables = messageVariables;
		return this;
	}


	// -------------------------------------------------- //


	public void playResult(Player player) {

		if (resultMessage != null) {
			player.sendMessage(MessagesUtils.get(resultMessage, messageVariables));
		}

		if (resultSound != null) {
			SoundsUtils.playSound(player, resultSound);
		}
	}


	// -------------------------------------------------- //
}
