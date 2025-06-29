package fr.snipertvmc.essentialsxgui.infrastructure.models;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import org.bukkit.Material;

import java.util.*;

public class EXGServer {


	// -------------------------------------------------- //


	private final MCServerVersion version;

	private Set<EXGKit> kits = new HashSet<>();


	// -------------------------------------------------- //


	public EXGServer() {
		this.version = Main.getInstance().getMCServerVersion();;
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
			}});
		});

		return kits;
	}


	public void setKitsRaw(Map<String, Object> kits) {
		kits.forEach((kitName, kitData) -> {
			EXGKit kit = new EXGKit(kitName);
			kit.setDisplayName((String) ((Map<String, Object>) kitData).get("displayName"));
			kit.setMaterial(Material.valueOf((String) ((Map<String, Object>) kitData).get("material")));
			this.kits.add(kit);
		});
	}


	// -------------------------------------------------- //
}
