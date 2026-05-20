package fr.snipertvmc.essentialsxgui.inventories.economy;

import com.earth2me.essentials.utils.NumberUtil;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEcoAction;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGMessage;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGSound;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.economy.EXGEcoAmountInventoryConfig;
import fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure.EXGItemConfig;
import fr.snipertvmc.essentialsxgui.libraries.fastinv.FastInv;
import fr.snipertvmc.essentialsxgui.utilities.InventoriesUtils;
import fr.snipertvmc.essentialsxgui.utilities.MessagesUtils;
import fr.snipertvmc.essentialsxgui.utilities.TextUtils;
import fr.snipertvmc.essentialsxgui.utilities.other.SoundsUtils;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class EcoAmountInventory extends FastInv {


	// -------------------------------------------------- //


	private final EXGEcoAmountInventoryConfig config = Main.getInstance().getInventoriesManager().getEcoAmountInventoryConfig().copy();

	private double totalAmountValue = 0;
	private double targetBalanceValue = 0;


	// -------------------------------------------------- //


	public EcoAmountInventory(Player player, Player target, EXGEcoAction ecoAction) {
		super(
				Main.getInstance().getInventoriesManager().getEcoAmountInventoryConfig().getRows() * 9,
				Main.getInstance().getInventoriesManager().getEcoAmountInventoryConfig().getEXGTitle()
						.duplicate()
						.updateVariables(Map.of(
								"targetName", target.getName(),
								"ecoAction", TextUtils.firstLetterToUpperCase(
										MessagesUtils.getString(ecoAction.getActionName())
								)))
						.getTitle(player)
		);


		InventoriesUtils.initializeBorderItem(player, config, this);


		this.targetBalanceValue = Main.getInstance().getEssentials().getUser(target).getMoney().doubleValue();
		String ecoActionName = MessagesUtils.getString(ecoAction.getActionName());


		if (config.getCancelActionItem().isEnabled()) {
			setItem(config.getCancelActionItem().getSlot(), config.getCancelActionItem()
					.updateVariables(Map.of(
							"ecoAction", ecoActionName))
					.build(player), e -> {

				new EcoActionInventory(player, target).open(player);
				SoundsUtils.playSound(player, EXGSound.ACTION_CANCELED);
			});
		}


		addAmountItems(player, target, ecoAction);
		addConfirmItem(player, target, ecoAction);
	}


	// -------------------------------------------------- //


	private void addAmountItems(Player player, Player target, EXGEcoAction ecoAction) {

		String targetBalance = NumberUtil.displayCurrency(BigDecimal.valueOf(this.targetBalanceValue), Main.getInstance().getEssentials());
		String targetNewBalance = NumberUtil.displayCurrency(BigDecimal.valueOf(switch (ecoAction) {
			case ADD -> this.targetBalanceValue + this.totalAmountValue;
			case TAKE -> this.targetBalanceValue - this.totalAmountValue;
			case SET -> this.totalAmountValue;
			case RESET -> 0;
		}), Main.getInstance().getEssentials());
		String ecoActionName = MessagesUtils.getString(ecoAction.getActionName());
		String totalAmount = NumberUtil.displayCurrency(BigDecimal.valueOf(this.totalAmountValue), Main.getInstance().getEssentials());

		// true -> AddItem, false -> RemoveItem
		Map<EXGItemConfig, Boolean> amountItems = new HashMap<>();

		if (config.getAddItems().getFirst().isEnabled() && ecoAction != EXGEcoAction.RESET) {
			config.getAddItems().forEach(addItem -> amountItems.put(addItem, true));
		}
		if (config.getRemoveItems().getFirst().isEnabled() && ecoAction != EXGEcoAction.RESET) {
			config.getRemoveItems().forEach(removeItem -> amountItems.put(removeItem, false));
		}

		for (EXGItemConfig amountItem : amountItems.keySet()) {
			EXGItemConfig amountItemCopy = amountItem.duplicate();

			double amountValue = amountItemCopy.getAmountValue();
			String amount = NumberUtil.displayCurrency(BigDecimal.valueOf(amountValue), Main.getInstance().getEssentials());

			setItem(amountItemCopy.getSlot(), amountItemCopy
					.updateVariables(Map.of(
							"targetBalance", targetBalance,
							"targetNewBalance", targetNewBalance,
							"ecoAction", ecoActionName,
							"amount", amount,
							"totalAmount", totalAmount))
					.build(player), e -> {


				// Calculate total amount
				if (amountItems.get(amountItem)) this.totalAmountValue += amountValue;
				else this.totalAmountValue -= amountValue;


				// Check if total amount can be updated (not negative and not exceeding max balance)
				if (canUpdateTotalAmount(player, target, ecoAction)) SoundsUtils.playSound(player, EXGSound.GUI_CLICK);
				else SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);


				// Update items to display new total amount and new target balance if action is confirmed
				addAmountItems(player, target, ecoAction);
				addConfirmItem(player, target, ecoAction);
			});
		}
	}


	private void addConfirmItem(Player player, Player target, EXGEcoAction ecoAction) {

		if (config.getConfirmActionItem().isEnabled()) {

			String targetBalance = NumberUtil.displayCurrency(BigDecimal.valueOf(this.targetBalanceValue), Main.getInstance().getEssentials());

			double targetNewBalanceValue = switch (ecoAction) {
				case ADD -> this.targetBalanceValue + this.totalAmountValue;
				case TAKE -> this.targetBalanceValue - this.totalAmountValue;
				case SET -> this.totalAmountValue;
				case RESET -> 0;
			};
			String targetNewBalance = NumberUtil.displayCurrency(BigDecimal.valueOf(targetNewBalanceValue), Main.getInstance().getEssentials());

			String amount = NumberUtil.displayCurrency(BigDecimal.valueOf(this.totalAmountValue), Main.getInstance().getEssentials());

			setItem(config.getConfirmActionItem().getSlot(), config.getConfirmActionItem()
					.duplicate()
					.updateVariables(Map.of(
							"targetBalance", targetBalance,
							"targetNewBalance", targetNewBalance,
							"ecoAction", MessagesUtils.getString(ecoAction.getActionName()),
							"amount", amount))
					.build(player), e -> {

				executeAction(player, target, ecoAction);

				player.closeInventory();
				SoundsUtils.playSound(player, EXGSound.ACTION_SUCCESS);
			});
		}
	}


	private boolean canUpdateTotalAmount(Player player, Player target, EXGEcoAction ecoAction) {

		// Negative amount
		if (this.totalAmountValue < 0) {
			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.AMOUNT_MUST_BE_POSITIVE));
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			this.totalAmountValue = 0;
			return  false;

		} else SoundsUtils.playSound(player, EXGSound.GUI_CLICK);


		// Max balance exceeded
		if (ecoAction == EXGEcoAction.ADD || ecoAction == EXGEcoAction.SET) {
			double newBalanceValue = this.targetBalanceValue + this.totalAmountValue;
			double maxBalanceValue = Main.getInstance().getEssentials().getSettings().getMaxMoney().doubleValue();
			if (newBalanceValue > maxBalanceValue) {
				String maxBalance = NumberUtil.displayCurrency(BigDecimal.valueOf(maxBalanceValue), Main.getInstance().getEssentials());
				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.MAX_AMOUNT_LIMIT, Map.of(
						"maxAmount", maxBalance)));
				this.totalAmountValue = maxBalanceValue - this.targetBalanceValue;
				return false;
			}
		}


		// Negative balance without permission 'essentials.eco.loan'
		if ((ecoAction == EXGEcoAction.TAKE)
				&& this.targetBalanceValue - this.totalAmountValue < 0
				&& !target.hasPermission("essentials.eco.loan")) {

			TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.CANT_HAVE_NEGATIVE_BALANCE));
			SoundsUtils.playSound(player, EXGSound.ACTION_FAILURE);
			this.totalAmountValue = 0;
			return false;
		}


		// Min balance exceeded
		if (ecoAction == EXGEcoAction.TAKE) {
			double newBalanceValue = this.targetBalanceValue - this.totalAmountValue;
			double minBalanceValue = Main.getInstance().getEssentials().getSettings().getMinMoney().doubleValue();
			if (newBalanceValue < minBalanceValue) {
				String minBalance = NumberUtil.displayCurrency(BigDecimal.valueOf(minBalanceValue), Main.getInstance().getEssentials());
				TextUtils.sendMessageToCommandSender(player, MessagesUtils.getString(EXGMessage.MIN_AMOUNT_LIMIT, Map.of(
						"minAmount", minBalance)));
				this.totalAmountValue = this.targetBalanceValue - minBalanceValue;
				return false;
			}
		}

		return true;
	}


	private void executeAction(Player player, Player target, EXGEcoAction ecoAction) {
		String command = ecoAction.getCommand(target.getName(), this.totalAmountValue);
		player.performCommand(command);
	}


	// -------------------------------------------------- //
}