package com.liuyue.igny.mixins.logger;

import carpet.logging.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Logger.class)
public interface LoggerAccessor {
    @Accessor("subscribedOnlinePlayers")
    Map<String, String> getSubscribedOnlinePlayers();
}
