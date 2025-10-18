package fr.snipertvmc.essentialsxgui.infrastructure.models;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import fr.snipertvmc.essentialsxgui.utilities.serializers.ItemStackSerializer;
import fr.snipertvmc.essentialsxgui.utilities.type.TypeUtils;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EXGServer {


	// -------------------------------------------------- //


	private final MCServerVersion version;

	private Set<EXGKit> kits = new HashSet<>();


	// -------------------------------------------------- //


	public EXGServer() {
		this.version = Main.getInstance().getMCServerVersion();
	}


	// -------------------------------------------------- //


	public MCServerVersion getVersion() {
		return version;
	}


	// -------------------------------------------------- //


	public Set<EXGKit> getKits() {
		return kits;
	}


	public EXGKit getKit(String kitName) {
		return kits.stream()
				.filter(kit -> kit.getName().equals(kitName))
				.findFirst()
				.orElse(null);
	}


	public void setKits(Set<EXGKit> kits) {
		this.kits = kits;
	}


	// -------------------------------------------------- //


	public Map<String, Object> getKitsRaw() {

		Map<String, Object> kits = new HashMap<>();

		this.kits.forEach(kit -> {
			kits.put(kit.getName(), new HashMap<>() {{
				put("displayName", kit.getDisplayName());
				put("material", kit.getMaterial().toString());
				put("data", kit.getData());
				put("customItemStack", ItemStackSerializer.serialize(kit.getCustomItemStack()));
			}});
		});

		return kits;
	}


	public void setKitsRaw(Map<String, Object> kits) {
		kits.forEach((kitName, kitData) -> {
			EXGKit kit = new EXGKit(kitName);

			Object displayNameObject = ((Map<String, Object>) kitData).get("displayName");
			Object materialObject = ((Map<String, Object>) kitData).get("material");

			Object dataObject = ((Map<String, Object>) kitData).get("data");
			String dataString = dataObject != null ? dataObject.toString() : "0";

			kit.setDisplayName((String) displayNameObject);
			kit.setMaterial(Material.valueOf((String) materialObject));
			kit.setData(dataObject != null && TypeUtils.isByte(dataString) ? Byte.parseByte(dataString) : 0);

			String serializedItemStack = (String) ((Map<String, Object>) kitData).get("customItemStack");
			if (serializedItemStack != null) {
				kit.setCustomItemStack(ItemStackSerializer.deserialize(serializedItemStack)[0]);
			} else {
				kit.setCustomItemStack(null);
			}

			this.kits.add(kit);
		});
	}


	// -------------------------------------------------- //
}
