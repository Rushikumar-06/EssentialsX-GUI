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

import java.time.ZoneId;
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
		User user = Main.getInstance().getEssentials().getUser(target.getUniqueId());

		String name = user.getName();
		user.setDisplayNick();
		String uuid = user.getBase().getUniqueId().toString();

		double health = user.getBase().getHealth();
		double maxHealth = user.getBase().getMaxHealth();
		int foodLevel = user.getBase().getFoodLevel();
		float saturation = user.getBase().getSaturation();
		int experience = user.getBase().getTotalExperience();
		int level = user.getBase().getLevel();
		Location worldLocation = user.getBase().getLocation();
		String location = MessagesUtils.get(EXGMessage.LOCATION_FORMAT, Map.of(
				"world", worldLocation.getWorld().getName(),
				"x", String.valueOf((int) worldLocation.getX()),
				"y", String.valueOf((int) worldLocation.getY()),
				"z", String.valueOf((int) worldLocation.getZ()),
				"yaw", String.valueOf((int) worldLocation.getYaw()),
				"pitch", String.valueOf((int) worldLocation.getPitch())
		));

		Statistic PLAY_ONE_TICK = EnumUtil.getStatistic("PLAY_ONE_MINUTE", "PLAY_ONE_TICK");
		long playtimeMs = System.currentTimeMillis() - (user.getBase().getStatistic(PLAY_ONE_TICK) * 50L);
		String playtime = TimeUtils.formatDateDiffZoned(playtimeMs);

		boolean isEcoEnabled = !Main.getInstance().getEssentials().getSettings().isEcoDisabled();
		String money;
		if (isEcoEnabled) {
			money = AdventureUtil.parsed(NumberUtil.displayCurrency(user.getMoney(), Main.getInstance().getEssentials())).toString();
		} else {
			money = null;
		}

		boolean canSeeIPAddress = user.isAuthorized("essentials.whois.ip");
		String ipAddress = null;
		if (canSeeIPAddress) {
			ipAddress = user.getBase().getAddress().getAddress().toString();
		}

		GameMode gamemode = user.getBase().getGameMode();
		String gamemodeName = switch (gamemode) {
			case SURVIVAL -> MessagesUtils.get(EXGMessage.GAMEMODE_SURVIVAL);
			case CREATIVE -> MessagesUtils.get(EXGMessage.GAMEMODE_CREATIVE);
			case ADVENTURE -> MessagesUtils.get(EXGMessage.GAMEMODE_ADVENTURE);
			case SPECTATOR -> MessagesUtils.get(EXGMessage.GAMEMODE_SPECTATOR);
		};
		boolean isGodMode = user.isGodModeEnabled();
		boolean canFly = user.getBase().getAllowFlight();
		boolean isFlying = user.getBase().isFlying();
		float walkSpeed = user.getBase().getWalkSpeed();
		float flySpeed = user.getBase().getFlySpeed();
		boolean isWhitelisted = user.getBase().isWhitelisted();
		boolean isOperator = user.getBase().isOp();
		boolean isVanished = user.isVanished();
		String nick = user.getNickname();
		boolean isNicked = nick != null && !nick.equals(name);
		boolean isAfk = user.isAfk();
		String afkSince = TimeUtils.formatDateDiffZoned(user.getAfkSince());

		boolean isJailed = user.isJailed();
		String jailName = user.getFormattedJailTime();
		String jailExpiry = TimeUtils.formatDateDiffZoned(user.getJailTimeout());
		boolean isMuted = user.isMuted();
		String muteReason = user.getMuteReason();
		String muteExpiry = TimeUtils.formatDateDiffZoned(user.getMuteTimeout());

		boolean isBanned = Bukkit.getServer().getBanList(BanList.Type.NAME).getBanEntry(target.getName()) != null;
		String banReason = MessagesUtils.get(EXGMessage.NOT_BANNED);
		String banExpiry = MessagesUtils.get(EXGMessage.NOT_BANNED);

		if (isBanned) {
			BanEntry banEntry = Bukkit.getServer().getBanList(BanList.Type.NAME).getBanEntry(target.getName());
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

		// Setting items
		if (config.getPlayerIdentificationItem().isEnabled()) {
			setItem(config.getPlayerIdentificationItem().getSlot(), config.getPlayerIdentificationItem()
					.updateVariables(Map.of(
							"targetName", name,
							"targetUUID", uuid,
							"targetIP", canSeeIPAddress ? ipAddress : "§c" + MessagesUtils.get(EXGMessage.HIDDEN),
							"targetPlaytime", playtime))
					.build()
			);
		}

		if (config.getPlayerStatisticsItem().isEnabled()) {
			setItem(config.getPlayerStatisticsItem().getSlot(), config.getPlayerStatisticsItem()
					.updateVariables(Map.of(
							"targetName", name,
							"targetHealth", String.valueOf(health),
							"targetMaxHealth", String.valueOf(maxHealth),
							"targetFoodLevel", String.valueOf(foodLevel),
							"targetSaturation", String.valueOf(saturation),
							"targetExperience", String.valueOf(experience),
							"targetLevel", String.valueOf(level)))
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
							put("targetMoney", isEcoEnabled ? money : MessagesUtils.get(EXGMessage.DISABLED));
							put("targetIsInGodMode", isGodMode ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
							put("targetCanFly", canFly ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
							put("targetIsFlying", isFlying ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
							put("targetWalkSpeed", String.valueOf(walkSpeed));
							put("targetFlySpeed", String.valueOf(flySpeed));
							put("targetIsOperator", isOperator ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
							put("targetIsWhitelisted", isWhitelisted ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
							put("targetIsVanished", isVanished ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
							put("targetIsNicked", isNicked ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
							put("targetNickname", isNicked ? nick : MessagesUtils.get(EXGMessage.NO_NICKNAME));
							put("targetIsAfk", isAfk ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
							put("targetAfkSince", isAfk ? afkSince : MessagesUtils.get(EXGMessage.NOT_AFK));
					}})
					.build()
			);
		}

		if (config.getPlayerPunishmentsItem().isEnabled()) {
			String finalBanReason = banReason;
			String finalBanExpiry = banExpiry;
			setItem(config.getPlayerPunishmentsItem().getSlot(), config.getPlayerPunishmentsItem()
					.updateVariables(new HashMap<>() {{
						put("targetName", name);
						put("targetIsJailed", isJailed ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
						put("targetJailName", isJailed ? jailName : MessagesUtils.get(EXGMessage.NOT_JAILED));
						put("targetJailExpiry", isJailed ? jailExpiry : MessagesUtils.get(EXGMessage.NOT_JAILED));
						put("targetIsMuted", isMuted ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
						put("targetMuteReason", isMuted ? (muteReason != null ? muteReason : MessagesUtils.get(EXGMessage.NO_MUTE_REASON)) : MessagesUtils.get(EXGMessage.NOT_MUTED));
						put("targetMuteExpiry", isMuted ? muteExpiry : MessagesUtils.get(EXGMessage.NOT_MUTED));
						put("targetIsBanned", isBanned ? MessagesUtils.get(EXGMessage.YES) : MessagesUtils.get(EXGMessage.NO));
						put("targetBanReason", finalBanReason);
						put("targetBanExpiry", finalBanExpiry);
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


//		player.sendMessage("§a--- Player Info: §f" + name + " §a---");
//		player.sendMessage("§aDisplay Name: §f" + displayName);
//		player.sendMessage("§aUUID: §f" + uuid);
//		player.sendMessage("§aHealth: §f" + health);
//		player.sendMessage("§aFood Level: §f" + foodLevel);
//		player.sendMessage("§aSaturation: §f" + saturation);
//		player.sendMessage("§aExperience: §f" + experience + " §a(Level: §f" + level + "§a)");
//		player.sendMessage("§aWorld Location: §f" + worldLocation.getWorld().getName() + " §aX: §f" + worldLocation.getX() + " §aY: §f" + worldLocation.getY() + " §aZ: §f" + worldLocation.getZ());
//		player.sendMessage("§aPlaytime: §f" + (playtime));
//		if (isEcoEnabled) {
//			player.sendMessage("§aMoney: §f" + money);
//		}
//		if (canSeeIPAddress) {
//			player.sendMessage("§aIP Address: §f" + ipAddress);
//		}
//		if (location != null) {
//			player.sendMessage("§aLocation: §f" + location);
//		}
//		player.sendMessage("§aGamemode: §f" + gamemode);
//		player.sendMessage("§aGod Mode: §f" + isGodMode);
//		player.sendMessage("§aOperator: §f" + isOperator);
//		player.sendMessage("§aCan Fly: §f" + canFly);
//		player.sendMessage("§aIs Flying: §f" + isFlying);
//		player.sendMessage("§aWalk Speed: §f" + walkSpeed);
//		player.sendMessage("§aFly Speed: §f" + flySpeed);
//		player.sendMessage("§aWhitelisted: §f" + isWhitelisted);
//		player.sendMessage("§aAFK: §f" + isAfk + (isAfk ? " §a(Since: §f" + afkSince + "§a)" : ""));
//		player.sendMessage("§aJailed: §f" + isJailed + (isJailed ? " §a(Jail: §f" + jailName + ", Timeout: §f" + (jailTimeout > 0 ? jailTimeout + "ms" : "Permanent") + "§a)" : ""));
//		player.sendMessage("§aMuted: §f" + isMuted + (isMuted ? " §a(Timeout: §f" + (muteTimeout != null ? muteTimeout : "Permanent") + "§a, Reason: §f" + (muteReason != null ? muteReason : "No reason specified") + "§a)" : ""));
//		player.sendMessage("§a------------------------------");
	}
}