package fr.snipertvmc.essentialsxgui.utilities;

import com.earth2me.essentials.libs.kyori.adventure.text.Component;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;

import java.util.Map;

public class MessagesUtils {


	// -------------------------------------------------- //


	public static String getString(EXGMessage exgMessage) {
		return getString(exgMessage, null);
	}


	public static String getString(EXGMessage exgMessage, Map<String, String> variables) {

		String prefix = Main.getInstance().getFilesManager().getMessages().getPrefix();
		String message = Main.getInstance().getFilesManager().getMessages().getString(exgMessage.getPath(),
				"§cMessage not found! Try to reset your messages.yml file, if the problem persists, contact plugin support."
		);
		String finalMessage = message.replace("{prefix}", prefix);

		if (variables != null) {
			for (Map.Entry<String, String> entry : variables.entrySet()) {
				finalMessage = finalMessage.replace("{" + entry.getKey() + "}", entry.getValue());
			}
		}

		return finalMessage;
	}


	// -------------------------------------------------- //


	public static Component getComponent(EXGMessage exgMessage) {
		return getComponent(exgMessage, null);
	}


	public static Component getComponent(EXGMessage exgMessage, Map<String, String> variables) {
		return TextUtils.getComponent(getString(exgMessage, variables));
	}


	// -------------------------------------------------- //
}
