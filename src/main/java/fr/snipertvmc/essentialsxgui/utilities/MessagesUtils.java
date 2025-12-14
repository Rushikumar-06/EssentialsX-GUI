package fr.snipertvmc.essentialsxgui.utilities;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;

import java.io.Console;
import java.util.Map;

public class MessagesUtils {


	// -------------------------------------------------- //


	public static String get(EXGMessage exgMessage) {
		return get(exgMessage, null);
	}


	public static String get(EXGMessage exgMessage, Map<String, String> variables) {

		String prefix = Main.getInstance().getFilesManager().getMessages().getPrefix();

		String message = Main.getInstance().getFilesManager().getMessages().getString(exgMessage.getPath(),
				"§cMessage not found! Try to reset your messages.yml file, if the problem persists, contact plugin support."
		);

		String finalMessage = message
				.replace("{prefix}", prefix)
				.replace("&", "§");

		if (variables == null) {
			return finalMessage;
		}

		for (Map.Entry<String, String> entry : variables.entrySet()) {
			finalMessage = finalMessage.replace("{" + entry.getKey() + "}", entry.getValue());
		}

		return finalMessage;
	}


	// -------------------------------------------------- //


	public static String removeColorCodes(String message) {
		return message
				.replaceAll("§[0-9a-fk-or]", "")
				.replaceAll("&[0-9a-fk-or]", "");
	}


	// -------------------------------------------------- //
}
