package fr.snipertvmc.essentialsxgui.utilities.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapUtils {


	// ---------------------------------------- //


	@Deprecated
	public static List<?> getKeys(Map<?, ?> map) {
		return new ArrayList<>(map.keySet());
	}


	@Deprecated
	public static List<?> getValues(Map<?, ?> map) {
		return new ArrayList<>(map.values());
	}


	public static Object getKeyWithValue(Map<?, ?> map, Object value) {
		return map.entrySet().stream()
				.filter(entry -> Objects.equals(entry.getValue(), value))
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(null);
	}


	// ---------------------------------------- //
}
