package com.liuyue.igny.client;

import com.liuyue.igny.client.renderer.world.HighlightBlocksRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class IGNYClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        IGNYClientRegister.register();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> HighlightBlocksRenderer.INSTANCE.clear());
    }
}
