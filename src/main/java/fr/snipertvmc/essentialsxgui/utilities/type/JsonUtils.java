package fr.snipertvmc.essentialsxgui.utilities.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {


	// -------------------------------------------------- //


	public static String listToJson(List<String> list) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(list);
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion de la liste en JSON", e);
		}
	}


	public static List<String> jsonToList(String json) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(json, new TypeReference<>() {});
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion du JSON en liste", e);
		}
	}

	// -------------------------------------------------- //


	public static String mapToJson(Map<String, Object> map) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(map);
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion de la map en JSON", e);
		}
	}


	public static Map<String, Object> jsonToMap(String json) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(json, new TypeReference<>() {});
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion du JSON en map", e);
		}
	}


	// -------------------------------------------------- //
}