package com.liuyue.igny.mixins.commands.customItemMaxStackSize;

import com.liuyue.igny.IGNYSettings;
import com.liuyue.igny.data.CustomItemMaxStackSizeDataManager;
import com.liuyue.igny.utils.InventoryUtils;
import com.liuyue.igny.utils.RuleUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInstance.class)
public interface ItemInstanceMixin {
    @Inject(method = "getMaxStackSize", at = @At(value = "RETURN"), cancellable = true)
    private void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        ItemInstance self = (ItemInstance) this;
        if (self instanceof ItemStack itemStack) {
            Item item = itemStack.getItem();
            if (cir.getReturnValue() == item.getDefaultMaxStackSize()) {
                int customMax = CustomItemMaxStackSizeDataManager.getCustomStackSize(itemStack);
                if (IGNYSettings.itemStackCountChanged.get() && customMax != -1) {
                    cir.setReturnValue(customMax);
                } else if (ShulkerBoxStackableRuleEnabled(itemStack)) {
                    cir.setReturnValue(item.getDefaultMaxStackSize());
                }
            }
        }
    }

    @Unique
    private boolean ShulkerBoxStackableRuleEnabled(ItemStack itemStack) {
        return Boolean.TRUE.equals(RuleUtils.getCarpetRulesValue("carpet-org-addition", "shulkerBoxStackable"))
                && InventoryUtils.isShulkerBoxItem(itemStack)
                && InventoryUtils.isEmptyShulkerBox(itemStack);
    }
}
