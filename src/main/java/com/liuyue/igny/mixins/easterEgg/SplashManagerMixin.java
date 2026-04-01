package com.liuyue.igny.mixins.easterEgg;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {
    @Shadow
    @Final
    private List<String> splashes;

    @WrapMethod(method = "getSplash")
    private SplashRenderer getSplash(Operation<SplashRenderer> original) {
        String currentLang = Minecraft.getInstance().getLanguageManager().getSelected();
        List<String> splashes = List.copyOf(this.splashes);
        if (currentLang.contains("zh")) {
            this.splashes.add("关注六月谢谢喵！！");
        } else {
            this.splashes.add("Follow Liuyue_awa!!");
        }
        SplashRenderer result = original.call();
        this.splashes.clear();
        this.splashes.addAll(splashes);
        return result;
    }
}
