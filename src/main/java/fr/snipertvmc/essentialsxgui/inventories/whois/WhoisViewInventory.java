package fr.snipertvmc.essentialsxgui.inventories.whois;

import com.earth2me.essentials.User;
import com.earth2me.essentials.utils.AdventureUtil;
import com.earth2me.essentials.utils.EnumUtil;
import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.whois.EXGWhoisViewInventoryConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.data.TimeUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WhoisViewInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGWhoisViewInventoryConfig config = Main.getInstance().getInventoriesManager().getWhoisViewInventoryConfig().copy();


	// -------------------------------------------------- //


	public WhoisViewInventory(Player player, Player target) {
		super(
				Main.getInstance().getInventoriesManager().getWhoisViewInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getWhoisViewInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of("targetName", target.getName()))
						.getTitle()
		);


		// Setting border
		if (config.getBorderItem().isEnabled()) {
			setItems(config.getBorderSlots(), config.getBorderItem().build());
		}


		// Fetching user data
		Map<String, String> playerData = getPlayerData(player);

		String name = target.getName();
		String uuid = target.getUniqueId().toString();
		String ipAddress = playerData.get("ipAddress");
		String playtime = playerData.get("playtime");

		String health = playerData.get("health");
		String maxHealth = playerData.get("maxHealth");
		String foodLevel = playerData.get("foodLevel");
		String saturation = playerData.get("saturation");
		String experience = playerData.get("experience");
		String level = playerData.get("level");

		Location worldLocation = target.getLocation();
		String location = playerData.get("location");

		String money = playerData.get("money");
		String gamemodeName = playerData.get("gamemodeName");
		String isGodMode = playerData.get("isGodMode");
		String canFly = playerData.get("canFly");
		String isFlying = playerData.get("isFlying");
		String walkSpeed = playerData.get("walkSpeed");
		String flySpeed = playerData.get("flySpeed");
		String isOperator = playerData.get("isOperator");
		String isWhitelisted = playerData.get("isWhitelisted");
		String isVanished = playerData.get("isVanished");
		String nickname = playerData.get("nickname");
		String isNicked = playerData.get("isNicked");
		String isAfk = playerData.get("isAfk");
		String afkSince = playerData.get("afkSince");

		String isJailed = playerData.get("isJailed");
		String jailName = playerData.get("jailName");
		String jailExpiry = playerData.get("jailExpiry");
		String isMuted = playerData.get("isMuted");
		String muteReason = playerData.get("muteReason");
		String muteExpiry = playerData.get("muteExpiry");
		String isBanned = playerData.get("isBanned");
		String banReason = playerData.get("banReason");
		String banExpiry = playerData.get("banExpiry");


		// Setting items
		if (config.getPlayerIdentificationItem().isEnabled()) {
			setItem(config.getPlayerIdentificationItem().getSlot(), config.getPlayerIdentificationItem()
					.updateVariables(Map.of(
							"targetName", name,
							"targetUUID", uuid,
							"targetIP", ipAddress,
							"targetPlaytime", playtime))
					.build()
			);
		}

		if (config.getPlayerStatisticsItem().isEnabled()) {
			setItem(config.getPlayerStatisticsItem().getSlot(), config.getPlayerStatisticsItem()
					.updateVariables(Map.of(
							"targetName", name,
							"targetHealth", health,
							"targetMaxHealth", maxHealth,
							"targetFoodLevel", foodLevel,
							"targetSaturation", saturation,
							"targetExperience", experience,
							"targetLevel", level))
					.build()
			);
		}

		if (config.getPlayerWorldItem().isEnabled()) {
			setItem(config.getPlayerWorldItem().getSlot(), config.getPlayerWorldItem()
					.updateVariables(Map.of(
							"targetName", name,
							"targetWorld", worldLocation.getWorld().getName(),
							"targetX", String.valueOf((int) worldLocation.getX()),
							"targetY", String.valueOf((int) worldLocation.getY()),
							"targetZ", String.valueOf((int) worldLocation.getZ()),
							"targetYaw", String.valueOf((int) worldLocation.getYaw()),
							"targetPitch", String.valueOf((int) worldLocation.getPitch()),
							"targetLocation", location))
					.build()
			);
		}

		if (config.getPlayerServerDataItem().isEnabled()) {
			setItem(config.getPlayerServerDataItem().getSlot(), config.getPlayerServerDataItem()
					.updateVariables(new HashMap<>() {{
							put("targetName", name);
							put("targetGamemode", gamemodeName);
							put("targetMoney", money);
							put("targetIsInGodMode", isGodMode);
							put("targetCanFly", canFly);
							put("targetIsFlying", isFlying);
							put("targetWalkSpeed", walkSpeed);
							put("targetFlySpeed", flySpeed);
							put("targetIsOperator", isOperator);
							put("targetIsWhitelisted", isWhitelisted);
							put("targetIsVanished", isVanished);
							put("targetIsNicked", isNicked);
							put("targetNickname", nickname);
							put("targetIsAfk", isAfk);
							put("targetAfkSince", afkSince);
					}})
					.build()
			);
		}

		if (config.getPlayerPunishmentsItem().isEnabled()) {
			setItem(config.getPlayerPunishmentsItem().getSlot(), config.getPlayerPunishmentsItem()
					.updateVariables(new HashMap<>() {{
						put("targetName", name);
						put("targetIsJailed", isJailed);
						put("targetJailName", jailName);
						put("targetJailExpiry", jailExpiry);
						put("targetIsMuted", isMuted);
						put("targetMuteReason", muteReason);
						put("targetMuteExpiry", muteExpiry);
						put("targetIsBanned", isBanned);
						put("targetBanReason", banReason);
						put("targetBanExpiry", banExpiry);
					}})
					.build()
			);
		}

		if (config.getBackItem().isEnabled()) {
			setItem(config.getBackItem().getSlot(), config.getBackItem().build(), e -> {

				new WhoisPlayersInventory(player).open(player);
				SoundsUtils.playSound(player, EXGSound.GUI_BACK);
			});
		}
	}


	// -------------------------------------------------- //


	public Map<String, String> getPlayerData(Player player) {
		User user = Main.getInstance().getEssentials().getUser(player.getUniqueId());

		Map<String, String> data = new HashMap<>();

		data.putAll(getPlayerIdentification(user));
		data.putAll(getPlayerStatistics(user));
		data.putAll(getPlayerWorld(user));
		data.putAll(getPlayerServerData(user, player));
		data.putAll(getPlayerPunishments(user, player));

		return data;
	}


	private Map<String, String> getPlayerIdentification(User user) {
		Map<String, String> map = new HashMap<>();

		boolean canSeeIPAddress = user.isAuthorized("essentials.whois.ip");
		String ipAddress = canSeeIPAddress
				? user.getBase().getAddress().getAddress().toString()
				: "§c" + MessagesUtils.get(EXGMessage.HIDDEN);

		Statistic PLAY_ONE_TICK = EnumUtil.getStatistic("PLAY_ONE_MINUTE", "PLAY_ONE_TICK");
		long playtimeMs = System.currentTimeMillis()
				- (user.getBase().getStatistic(PLAY_ONE_TICK) * 50L);
		String playtime = TimeUtils.formatDateDiffZoned(playtimeMs);

		map.put("ipAddress", ipAddress);
		map.put("playtime", playtime);

		return map;
	}


	private Map<String, String> getPlayerStatistics(User user) {
		Map<String, String> map = new HashMap<>();

		map.put("health", String.valueOf(user.getBase().getHealth()));
		map.put("maxHealth", String.valueOf(user.getBase().getMaxHealth()));
		map.put("foodLevel", String.valueOf(user.getBase().getFoodLevel()));
		map.put("saturation", String.valueOf(user.getBase().getSaturation()));
		map.put("experience", String.valueOf(user.getBase().getTotalExperience()));
		map.put("level", String.valueOf(user.getBase().getLevel()));

		return map;
	}


	private Map<String, String> getPlayerWorld(User user) {
		Map<String, String> map = new HashMap<>();

		Location loc = user.getBase().getLocation();
		String location = MessagesUtils.get(EXGMessage.LOCATION_FORMAT, Map.of(
				"world", loc.getWorld().getName(),
				"x", String.valueOf((int) loc.getX()),
				"y", String.valueOf((int) loc.getY()),
				"z", String.valueOf((int) loc.getZ()),
				"yaw", String.valueOf((int) loc.getYaw()),
				"pitch", String.valueOf((int) loc.getPitch())
		));

		map.put("location", location);

		return map;
	}


	private Map<String, String> getPlayerServerData(User user, Player player) {
		Map<String, String> map = new HashMap<>();

		GameMode gamemode = user.getBase().getGameMode();
		String gamemodeName = switch (gamemode) {
			case SURVIVAL -> MessagesUtils.get(EXGMessage.GAMEMODE_SURVIVAL);
			case CREATIVE -> MessagesUtils.get(EXGMessage.GAMEMODE_CREATIVE);
			case ADVENTURE -> MessagesUtils.get(EXGMessage.GAMEMODE_ADVENTURE);
			case SPECTATOR -> MessagesUtils.get(EXGMessage.GAMEMODE_SPECTATOR);
		};

		boolean ecoEnabled = !Main.getInstance().getEssentials().getSettings().isEcoDisabled();
		String money = ecoEnabled
				? AdventureUtil.parsed(
				NumberUtil.displayCurrency(user.getMoney(), Main.getInstance().getEssentials())
		).toString()
				: MessagesUtils.get(EXGMessage.DISABLED);

		String nick = user.getNickname();
		boolean isNicked = nick != null && !nick.equals(player.getName());
		boolean isAfk = user.isAfk();

		map.put("money", money);
		map.put("gamemodeName", gamemodeName);
		map.put("isGodMode", yesNo(user.isGodModeEnabled()));
		map.put("canFly", yesNo(user.getBase().getAllowFlight()));
		map.put("isFlying", yesNo(user.getBase().isFlying()));
		map.put("walkSpeed", String.valueOf(user.getBase().getWalkSpeed()));
		map.put("flySpeed", String.valueOf(user.getBase().getFlySpeed()));
		map.put("isOperator", yesNo(user.getBase().isOp()));
		map.put("isWhitelisted", yesNo(user.getBase().isWhitelisted()));
		map.put("isVanished", yesNo(user.isVanished()));
		map.put("nickname", isNicked ? nick : MessagesUtils.get(EXGMessage.NO_NICKNAME));
		map.put("isNicked", yesNo(isNicked));
		map.put("isAfk", yesNo(isAfk));
		map.put("afkSince", isAfk
				? TimeUtils.formatDateDiffZoned(user.getAfkSince())
				: MessagesUtils.get(EXGMessage.NOT_AFK));

		return map;
	}


	private Map<String, String> getPlayerPunishments(User user, Player player) {
		Map<String, String> map = new HashMap<>();

		boolean isJailed = user.isJailed();
		map.put("isJailed", yesNo(isJailed));
		map.put("jailName", user.getFormattedJailTime());
		map.put("jailExpiry", isJailed
				? TimeUtils.formatDateDiffZoned(user.getJailTimeout())
				: MessagesUtils.get(EXGMessage.NOT_JAILED));

		boolean isMuted = user.isMuted();
		map.put("isMuted", yesNo(isMuted));
		map.put("muteReason", isMuted
				? (user.getMuteReason() != null
				? user.getMuteReason()
				: MessagesUtils.get(EXGMessage.NO_MUTE_REASON))
				: MessagesUtils.get(EXGMessage.NOT_MUTED));
		map.put("muteExpiry", isMuted
				? TimeUtils.formatDateDiffZoned(user.getMuteTimeout())
				: MessagesUtils.get(EXGMessage.NOT_MUTED));

		BanEntry banEntry = Bukkit.getServer()
				.getBanList(BanList.Type.NAME)
				.getBanEntry(player.getName());

		boolean isBanned = banEntry != null;
		map.put("isBanned", yesNo(isBanned));
		map.put("banReason", isBanned
				? (banEntry.getReason() != null && !banEntry.getReason().isEmpty()
				? banEntry.getReason()
				: MessagesUtils.get(EXGMessage.NO_BAN_REASON))
				: MessagesUtils.get(EXGMessage.NOT_BANNED));
		map.put("banExpiry", isBanned
				? (banEntry.getExpiration() != null
				? TimeUtils.formatDateDiffZoned(banEntry.getExpiration().getTime())
				: MessagesUtils.get(EXGMessage.PERMANENT))
				: MessagesUtils.get(EXGMessage.NOT_BANNED));

		return map;
	}


	// -------------------------------------------------- //


	private String yesNo(boolean value) {
		return value
				? MessagesUtils.get(EXGMessage.YES)
				: MessagesUtils.get(EXGMessage.NO);
	}


	// -------------------------------------------------- //
}