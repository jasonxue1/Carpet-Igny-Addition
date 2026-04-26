package com.liuyue.igny.manager;

import com.google.gson.reflect.TypeToken;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AmethystVaultManager extends BaseDataManager<AmethystVaultManager.VaultData> {
    public static final AmethystVaultManager INSTANCE = new AmethystVaultManager();

    private VaultData data;
    private boolean dirty = false;

    public static class VaultData {
        public Map<Long, String> vault = new HashMap<>();
        public Set<Long> pendingRestore = new HashSet<>();
    }

    @Override protected String getFileName() { return "amethyst_vault.json"; }
    @Override protected Type getDataType() { return new TypeToken<VaultData>(){}.getType(); }
    @Override public VaultData getDefaultData() { return new VaultData(); }

    @Override
    protected void applyData(VaultData data) {
        this.data = data != null ? data : new VaultData();
        this.dirty = false;
    }

    @Override
    public VaultData getCurrentData() {
        if (this.data == null) this.data = new VaultData();
        return this.data;
    }

    @Override protected StorageScope getScope() { return StorageScope.WORLD; }
    @Override protected SideRestraint getSideRestraint() { return SideRestraint.COMMON; }

    public void storeBud(BlockPos pos, BlockState state) {
        String id = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
        getCurrentData().vault.put(pos.asLong(), id);
        this.dirty = true;
    }

    public boolean has(BlockPos pos) {
        return getCurrentData().vault.containsKey(pos.asLong());
    }

    public BlockState getAndRemove(BlockPos pos) {
        VaultData vData = getCurrentData();
        String id = vData.vault.remove(pos.asLong());
        boolean removedFromPending = vData.pendingRestore.remove(pos.asLong());

        if (id == null && !removedFromPending) return null;

        this.dirty = true;
        if (id == null) return null;

        ResourceLocation rl = ResourceLocation.tryParse(id);
        if (rl == null) return null;

        Block block = BuiltInRegistries.BLOCK.
                //#if MC >= 12102
                //$$ getValue(rl);
                //#else
                        get(rl);
        //#endif
        return (block != Blocks.AIR) ? block.defaultBlockState() : null;
    }

    public void markPending(BlockPos pos) {
        VaultData vData = getCurrentData();
        if (vData.vault.containsKey(pos.asLong())) {
            if (vData.pendingRestore.add(pos.asLong())) {
                this.dirty = true;
            }
        }
    }

    public void scheduledSave() {
        if (this.dirty) {
            this.save();
            this.dirty = false;
        }
    }

    public Set<Long> getPendingRestore() {
        return getCurrentData().pendingRestore;
    }
}