package com.liuyue.igny.logging;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;

public class IGNYLoggerRegistry {
    public static boolean __piston;
    public static boolean __allFurnace;
    public static boolean __beacon;

    public static void registerLoggers() {
        LoggerRegistry.registerLogger("piston", stardardLogger("piston", null, null, false));
        LoggerRegistry.registerLogger("allFurnace", stardardLogger("allFurnace", "0x32FF0000", new String[]{"0x32FF0000", "0x16FF0000", "0x32FFFFFF"}, false));
        LoggerRegistry.registerLogger("beacon", stardardLogger("beacon", null, null, false));
    }

    @SuppressWarnings("SameParameterValue")
    private static Logger stardardLogger(String logName, String def, String [] options, boolean strictOptions) {
        try {
            return new Logger(IGNYLoggerRegistry.class.getField("__" + logName), logName, def, options, strictOptions);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to create logger " + logName);
        }
    }
}
