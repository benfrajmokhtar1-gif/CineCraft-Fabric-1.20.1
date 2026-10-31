package com.cinecraft.client;

import com.cinecraft.CineCraft;
import net.fabricmc.api.ClientModInitializer;

public class CineCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CineCraft.LOGGER.info("CineCraft client initialized!");
    }
}
