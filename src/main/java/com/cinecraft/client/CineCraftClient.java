package com.cinecraft.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CineCraftClient implements ClientModInitializer {

    private static boolean sideCameraEnabled = false;
    private static KeyBinding toggleSideCameraKey;

    @Override
    public void onInitializeClient() {
        toggleSideCameraKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.cinecraft.toggle_side_camera",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.cinecraft"
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleSideCameraKey.wasPressed()) {
                sideCameraEnabled = !sideCameraEnabled;
            }
        });
    }

    public static boolean isSideCameraEnabled() {
        return sideCameraEnabled;
    }
}
