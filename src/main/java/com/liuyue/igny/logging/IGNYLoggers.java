package com.liuyue.igny.logging;

import com.liuyue.igny.logging.annotation.Logger;
import carpet.logging.LoggerRegistry;
import com.liuyue.igny.logging.callback.LoggerCallback;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class IGNYLoggers {
    private static final Map<String, LoggerCallback> callbacks = new HashMap<>();

    @Logger
    public static boolean piston;

    @Logger(
            defaultValue = "0x32FF0000",
            options = {"0x32FF0000", "0x16FF0000", "0x32FFFFFF"}
    )
    public static boolean allFurnace;

    @Logger
    public static boolean beacon;

    public static void registerLoggers() {
        for (Field field : IGNYLoggers.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Logger.class)) {
                continue;
            }
            Logger anno = field.getAnnotation(Logger.class);
            String defaultValue = "".equals(anno.defaultValue()) ? null : anno.defaultValue();
            String[] options = "".equals(anno.options()[0]) ? null : anno.options();
            boolean strictOptions = anno.strictOptions();
            LoggerRegistry.registerLogger(field.getName(), new carpet.logging.Logger(field, field.getName(), defaultValue, options, strictOptions));
            try {
                Class<? extends LoggerCallback> clazz = anno.callback();
                if (clazz != LoggerCallback.class) {
                    var constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    LoggerCallback instance = constructor.newInstance();
                    callbacks.put(field.getName(), instance);
                }
            } catch (Exception ignored) {}
        }
    }

    public static void handleChange(MinecraftServer server, carpet.logging.Logger logger, String playerName, String option, boolean subscribe) {
        LoggerCallback callback = callbacks.get(logger.getLogName());
        if (callback != null) {
            if (subscribe) {
                callback.onSubscribe(logger, server.getPlayerList().getPlayerByName(playerName), option);
            } else {
                callback.onUnsubscribe(logger, playerName);
            }
        }
    }
}
