package com.liuyue.igny.utils;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;
import java.util.Set;

public class IGNYMixinConfigPlugin extends RestrictiveMixinConfigPlugin {
    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.startsWith("com.liuyue.igny.mixins.carpet.fix.fakePlayerMemoryLeakFix")) {
            String carpetVersion = FabricLoader.getInstance().getModContainer("carpet").orElseThrow().getMetadata().getVersion().toString();
            if (carpetVersion.length() >= 6) {
                if (Integer.decode(carpetVersion.substring(carpetVersion.length() - 6)) >= 260326) {
                    return false;
                }
            }
        }
        return super.shouldApplyMixin(targetClassName, mixinClassName);
    }
}
