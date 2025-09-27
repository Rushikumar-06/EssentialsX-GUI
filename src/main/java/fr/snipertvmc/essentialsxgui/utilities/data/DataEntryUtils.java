package fr.snipertvmc.essentialsxgui.utilities.data;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryResult;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGEntrySettings;
import fr.snipertvmc.essentialsxgui.inventories.others.DataEntryAnvilInventory;
import fr.snipertvmc.essentialsxgui.inventories.others.DataEntryGUIInventory;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.utilities.type.TypeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.function.Consumer;

public class DataEntryUtils {


	// -------------------------------------------------- //


	public static void processStringEntry(Player player, EXGEntrySettings entrySettings,
	                                      Consumer<Pair<String, EXGEntryResult>> onSuccess,
	                                      Consumer<Pair<String, EXGEntryResult>> onFailure) {

		if (!entrySettings.getAcceptedTypes().contains(entrySettings.getType())) {
			onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED));
			throw new IllegalArgumentException("The entry type " + entrySettings.getType() + " is not accepted in the accepted types list.");
		}

		switch (entrySettings.getType()) {

			case CHAT -> Main.getInstance().getChatManager().addChat(player, entry -> {

				Pair<String, EXGEntryResult> result = checkStringEntry(entry, entrySettings);

				Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
					if (result.getRight() == EXGEntryResult.SUCCESS) {
						onSuccess.accept(result);
					} else {
						onFailure.accept(result);
						result.getRight().playResult(player);
					}
				});

			}, 10);

			case ANVIL -> new DataEntryAnvilInventory(player, entrySettings, onSuccess, onFailure);

			case GUI -> new DataEntryGUIInventory(player, entrySettings, onSuccess, onFailure).open(player);
		}

	}


	public static Pair<String, EXGEntryResult> checkStringEntry(String value, EXGEntrySettings entrySettings) {

		if (entrySettings.getEqualsToSomething() != null) {
			if (value.equalsIgnoreCase(entrySettings.getEqualsToSomething())) {
				return new Pair<>(value, EXGEntryResult.SUCCESS);
			}
			return new Pair<>(value, EXGEntryResult.CANCELED);
		}

		if (value.equalsIgnoreCase("cancel")) {
			return new Pair<>(value, EXGEntryResult.CANCELED);
		}

		if ( (entrySettings.getMinLength() != -1 && value.length() < entrySettings.getMinLength())
			|| (entrySettings.getMaxLength() != -1 && value.length() > entrySettings.getMaxLength()) ) {

			return new Pair<>(value, EXGEntryResult.LENGTH_LIMIT.setMessageVariables(
					Map.of("min", String.valueOf(entrySettings.getMinLength()),
							"max", String.valueOf(entrySettings.getMaxLength()))
			));
		}

		if (entrySettings.isMustBeNumber() && !TypeUtils.isInteger(value)) {
			return new Pair<>(value, EXGEntryResult.INVALID_NUMBER);
		}

//          /!\  WILL BE IMPLEMENTED LATER  /!\
//
//		if (entrySettings.getAcceptedCharacters() != null) {
//			for (char c : value.toCharArray()) {
//				boolean isAccepted = false;
//				for (char ac : entrySettings.getAcceptedCharacters()) {
//					if (c == ac) {
//						isAccepted = true;
//						break;
//					}
//				}
//				if (!isAccepted) {
//					return new Pair<>(value, EXGEntryResult.INVALID_CHARACTERS);
//				}
//			}
//		}

		return new Pair<>(value, EXGEntryResult.SUCCESS);
	}


	// -------------------------------------------------- //


	public static void processMaterialEntry(Player player, EXGEntrySettings entrySettings,
	                                        Consumer<Pair<Pair<Material, Byte>, EXGEntryResult>> onSuccess,
	                                        Consumer<Pair<Pair<Material, Byte>, EXGEntryResult>> onFailure) {

		if (!entrySettings.getAcceptedTypes().contains(entrySettings.getType())) {
			onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED));
			throw new IllegalArgumentException("The entry type " + entrySettings.getType() + " is not accepted in the accepted types list.");
		}

		switch (entrySettings.getType()) {

			case CHAT -> Main.getInstance().getChatManager().addChat(player, entry -> {

				Pair<Pair<Material, Byte>, EXGEntryResult> result = checkMaterialEntry(entry);

				Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
					if (result.getRight() == EXGEntryResult.SUCCESS) {
						onSuccess.accept(result);
					} else {
						onFailure.accept(result);
						result.getRight().playResult(player);
					}
				});

			}, 10);

			case ANVIL -> new DataEntryAnvilInventory(player, entrySettings,

					materialPairResult -> {

						Pair<Pair<Material, Byte>, EXGEntryResult> result = checkMaterialEntry(materialPairResult.getLeft());

						if (result.getRight() == EXGEntryResult.SUCCESS) {
							onSuccess.accept(result);
						} else {
							onFailure.accept(result);
							result.getRight().playResult(player);
						}

					},
					materialPairResult -> onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED))
			);

			case GUI -> new DataEntryGUIInventory(player, entrySettings,

					materialPairResult -> {

						Pair<Pair<Material, Byte>, EXGEntryResult> result = checkMaterialEntry(materialPairResult.getLeft());

						if (result.getRight() == EXGEntryResult.SUCCESS) {
							onSuccess.accept(result);
						} else {
							onFailure.accept(result);
							result.getRight().playResult(player);
						}

					},
					materialPairResult -> onFailure.accept(new Pair<>(null, EXGEntryResult.CANCELED))

			).open(player);
		}

	}


	public static Pair<Pair<Material, Byte>, EXGEntryResult> checkMaterialEntry(String value) {

		if (value.equalsIgnoreCase("cancel")) {
			return new Pair<>(null, EXGEntryResult.CANCELED);
		}

		Material material;
		byte data = 0;

		if (value.contains(":")) {

			String[] materialSplit = value.split(":");
			if (materialSplit.length != 2 || !TypeUtils.isByte(materialSplit[1])) {
				return new Pair<>(null, EXGEntryResult.INVALID_MATERIAL);
			}

			material = Material.matchMaterial(materialSplit[0]);
			data = Byte.parseByte(materialSplit[1]);

		} else {
			material = Material.matchMaterial(value);
		}

		if (material == null) {
			return new Pair<>(null, EXGEntryResult.INVALID_MATERIAL);
		}

		return new Pair<>(new Pair<>(material, data), EXGEntryResult.SUCCESS);
	}


	// -------------------------------------------------- //
}
