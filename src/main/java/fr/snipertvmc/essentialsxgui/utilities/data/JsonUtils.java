package fr.snipertvmc.essentialsxgui.utilities.data;

import com.fasterxml.jackson.core.type.TypeReference;
import fr.snipertvmc.essentialsxgui.Main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {


	// -------------------------------------------------- //


	public static String listToJson(List<String> list) {
		try {
			return Main.getInstance().getObjectMapper().writeValueAsString(list);
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion de la liste en JSON", e);
		}
	}


	public static List<String> jsonToList(String json) {
		try {
			return Main.getInstance().getObjectMapper().readValue(json, new TypeReference<>() {});
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion du JSON en liste", e);
		}
	}

	// -------------------------------------------------- //


	public static String mapToJson(Map<String, Object> map) {
		try {
			return Main.getInstance().getObjectMapper().writeValueAsString(map);
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion de la map en JSON", e);
		}
	}


	public static Map<String, Object> jsonToMap(String json) {
		try {
			return Main.getInstance().getObjectMapper().readValue(json, new TypeReference<>() {});
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion du JSON en map", e);
		}
	}

	// -------------------------------------------------- //
}