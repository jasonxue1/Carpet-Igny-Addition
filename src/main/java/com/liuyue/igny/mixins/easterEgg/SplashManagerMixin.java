package com.liuyue.igny.mixins.easterEgg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class SplashManagerMixin {
    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/List;", at = @At(value = "RETURN"), cancellable = true)
    private void prepare(ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfoReturnable<List<String>> cir) {
        List<String> extra = new ArrayList<>();
        String currentLang = Minecraft.getInstance().getLanguageManager().getSelected();
        if (currentLang.contains("zh")) {
            extra.add("关注六月谢谢喵！！");
        } else {
            extra.add("Follow Liuyue_awa!!");
        }
        LocalDate now = LocalDate.now();
        boolean birthday = now.getMonthValue() == 11 && now.getDayOfMonth() == 2;
        if (cir.getReturnValue() != null) {
            try {
                if (birthday) {
                    cir.getReturnValue().clear();
                    cir.getReturnValue().add("Happy birthday, Liuyue_awa!!!");
                } else {
                    cir.getReturnValue().addAll(extra);
                }
            } catch (UnsupportedOperationException ignored) {
                List<String> arrayListTexts = new ArrayList<>(cir.getReturnValue());
                if (birthday) {
                    arrayListTexts.clear();
                    arrayListTexts.add("Happy birthday, Liuyue_awa!!!");
                }
                arrayListTexts.addAll(extra);
                cir.setReturnValue(arrayListTexts);
            }
        }
    }
}
