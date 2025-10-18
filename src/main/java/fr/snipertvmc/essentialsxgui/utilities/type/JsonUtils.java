package fr.snipertvmc.essentialsxgui.utilities.type;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonUtils {


	// -------------------------------------------------- //


	private static final Moshi moshi = new Moshi.Builder().build();


	// -------------------------------------------------- //


	public static String listToJson(List<String> list) {
		Type type = Types.newParameterizedType(List.class, String.class);
		JsonAdapter<List<String>> adapter = moshi.adapter(type);
		return adapter.toJson(list);
	}


	public static List<String> jsonToList(String json) {
		try {
			Type type = Types.newParameterizedType(List.class, String.class);
			JsonAdapter<List<String>> adapter = moshi.adapter(type);
			return adapter.fromJson(json);
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion du JSON en liste", e);
		}
	}

	// -------------------------------------------------- //


	public static String mapToJson(Map<String, Object> map) {
		Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
		JsonAdapter<Map<String, Object>> adapter = moshi.adapter(type);
		return adapter.toJson(map);
	}


	public static Map<String, Object> jsonToMap(String json) {
		try {
			Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
			JsonAdapter<Map<String, Object>> adapter = moshi.adapter(type);
			return adapter.fromJson(json);
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la conversion du JSON en map", e);
		}
	}


	// -------------------------------------------------- //
}