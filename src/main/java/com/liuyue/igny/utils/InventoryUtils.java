package com.liuyue.igny.utils;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class InventoryUtils {
    public static boolean isShulkerBoxItem(ItemStack shulkerBox) {
        if (shulkerBox.is(Items.SHULKER_BOX)) {
            return true;
        }
        if (shulkerBox.getItem() instanceof BlockItem blockItem) {
            return blockItem.getBlock() instanceof ShulkerBoxBlock;
        }
        return false;
    }

    public static boolean isEmptyShulkerBox(ItemStack shulkerBox) {
        if (shulkerBox.getCount() != 1) {
            return true;
        }
        ItemContainerContents component = shulkerBox.get(DataComponents.CONTAINER);
        if (component == null || component == ItemContainerContents.EMPTY) {
            return true;
        }
        return !component.nonEmptyItems().iterator().hasNext();
    }
}
