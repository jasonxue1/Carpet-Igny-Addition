package com.liuyue.igny.client.utils;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ClientUtils {
    public static Minecraft getClient() {
        return Minecraft.getInstance();
    }

    @NotNull
    public static GameRenderer getGameRenderer() {
        return getClient().gameRenderer;
    }

    @Contract(pure = true)
    public static Camera getCamera() {
        return getGameRenderer().getMainCamera();
    }
}
