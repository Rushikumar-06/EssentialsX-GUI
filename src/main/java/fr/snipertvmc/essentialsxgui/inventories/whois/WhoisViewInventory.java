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


		// Player identification
		boolean canSeeIPAddress = user.isAuthorized("essentials.whois.ip");
		String ipAddress;
		if (canSeeIPAddress) {
			ipAddress = user.getBase().getAddress().getAddress().toString();
		} else {
			ipAddress = null;
		}

		Statistic PLAY_ONE_TICK = EnumUtil.getStatistic("PLAY_ONE_MINUTE", "PLAY_ONE_TICK");
		long playtimeMs = System.currentTimeMillis() - (user.getBase().getStatistic(PLAY_ONE_TICK) * 50L);
		String playtime = TimeUtils.formatDateDiffZoned(playtimeMs);


		// Player statistics
		double health = user.getBase().getHealth();
		double maxHealth = user.getBase().getMaxHealth();
		int foodLevel = user.getBase().getFoodLevel();
		float saturation = user.getBase().getSaturation();
		int experience = user.getBase().getTotalExperience();
		int level = user.getBase().getLevel();


		// Player world
		Location worldLocation = user.getBase().getLocation();
		String location = MessagesUtils.get(EXGMessage.LOCATION_FORMAT, Map.of(
				"world", worldLocation.getWorld().getName(),
				"x", String.valueOf((int) worldLocation.getX()),
				"y", String.valueOf((int) worldLocation.getY()),
				"z", String.valueOf((int) worldLocation.getZ()),
				"yaw", String.valueOf((int) worldLocation.getYaw()),
				"pitch", String.valueOf((int) worldLocation.getPitch())
		));


		// Player server data
		GameMode gamemode = user.getBase().getGameMode();
		String gamemodeName = switch (gamemode) {
			case SURVIVAL -> MessagesUtils.get(EXGMessage.GAMEMODE_SURVIVAL);
			case CREATIVE -> MessagesUtils.get(EXGMessage.GAMEMODE_CREATIVE);
			case ADVENTURE -> MessagesUtils.get(EXGMessage.GAMEMODE_ADVENTURE);
			case SPECTATOR -> MessagesUtils.get(EXGMessage.GAMEMODE_SPECTATOR);
		};

		boolean isEcoEnabled = !Main.getInstance().getEssentials().getSettings().isEcoDisabled();
		String money;
		if (isEcoEnabled) {
			money = AdventureUtil.parsed(NumberUtil.displayCurrency(user.getMoney(), Main.getInstance().getEssentials())).toString();
		} else {
			money = null;
		}

		boolean isGodMode = user.isGodModeEnabled();
		boolean canFly = user.getBase().getAllowFlight();
		boolean isFlying = user.getBase().isFlying();
		float walkSpeed = user.getBase().getWalkSpeed();
		float flySpeed = user.getBase().getFlySpeed();
		boolean isWhitelisted = user.getBase().isWhitelisted();
		boolean isOperator = user.getBase().isOp();
		boolean isVanished = user.isVanished();
		String nick = user.getNickname();
		boolean isNicked = nick != null && !nick.equals(player.getName());
		boolean isAfk = user.isAfk();
		String afkSince = TimeUtils.formatDateDiffZoned(user.getAfkSince());


		// Player punishments
		boolean isJailed = user.isJailed();
		String jailName = user.getFormattedJailTime();
		String jailExpiry = TimeUtils.formatDateDiffZoned(user.getJailTimeout());

		boolean isMuted = user.isMuted();
		String muteReason = user.getMuteReason();
		String muteExpiry = TimeUtils.formatDateDiffZoned(user.getMuteTimeout());

		boolean isBanned = Bukkit.getServer().getBanList(BanList.Type.NAME).getBanEntry(player.getName()) != null;
		String banReason = MessagesUtils.get(EXGMessage.NOT_BANNED);
		String banExpiry = MessagesUtils.get(EXGMessage.NOT_BANNED);
		if (isBanned) {
			BanEntry banEntry = Bukkit.getServer().getBanList(BanList.Type.NAME).getBanEntry(player.getName());
			if (banEntry != null) {

				if (banEntry.getReason() != null && !banEntry.getReason().isEmpty()) {
					banReason = banEntry.getReason();
				} else {
					banReason = MessagesUtils.get(EXGMessage.NO_BAN_REASON);
				}

				if (banEntry.getExpiration() != null) {
					banExpiry = TimeUtils.formatDateDiffZoned(banEntry.getExpiration().getTime());
				} else {
					banExpiry = MessagesUtils.get(EXGMessage.PERMANENT);
				}
			}
		}

		String finalBanReason = banReason;
		String finalBanExpiry = banExpiry;

		return new HashMap<>() {{
			put("ipAddress", canSeeIPAddress ? ipAddress : "§c" + MessagesUtils.get(EXGMessage.HIDDEN));
			put("playtime", playtime);

			put("health", String.valueOf(health));
			put("maxHealth", String.valueOf(maxHealth));
			put("foodLevel", String.valueOf(foodLevel));
			put("saturation", String.valueOf(saturation));
			put("experience", String.valueOf(experience));
			put("level", String.valueOf(level));

			put("location", location);

			put("money", isEcoEnabled ? money : MessagesUtils.get(EXGMessage.DISABLED));
			put("gamemodeName", gamemodeName);
			put("isGodMode", isGodMode ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("canFly", canFly ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("isFlying", isFlying ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("walkSpeed", String.valueOf(walkSpeed));
			put("flySpeed", String.valueOf(flySpeed));
			put("isOperator", isOperator ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("isWhitelisted", isWhitelisted ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("isVanished", isVanished ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("nickname", isNicked ? nick : MessagesUtils.get(EXGMessage.NO_NICKNAME));
			put("isNicked", isNicked ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("isAfk", isAfk ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("afkSince", isAfk ? afkSince : MessagesUtils.get(EXGMessage.NOT_AFK));

			put("isJailed", isJailed ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("jailName", jailName);
			put("jailExpiry", isJailed ? jailExpiry : MessagesUtils.get(EXGMessage.NOT_JAILED));
			put("isMuted", isMuted ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("muteReason", isMuted ? (muteReason != null ? muteReason : MessagesUtils.get(EXGMessage.NO_MUTE_REASON)) : MessagesUtils.get(EXGMessage.NOT_MUTED));
			put("muteExpiry", isMuted ? muteExpiry : MessagesUtils.get(EXGMessage.NOT_MUTED));
			put("isBanned", isBanned ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
			put("banReason", finalBanReason);
			put("banExpiry", finalBanExpiry);
		}};
	}
}