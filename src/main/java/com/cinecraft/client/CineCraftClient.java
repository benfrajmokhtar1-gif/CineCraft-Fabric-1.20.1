package com.cinecraft.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CineCraftClient implements ClientModInitializer {

    public enum CameraMode {
        SIDE,
        TOP,
        LOW,
        ORBIT,
        FRONT,
        WIDE,
        CLOSE_UP,
        TRACKING
    }

    private static boolean cameraEnabled = false;
    private static CameraMode currentMode = CameraMode.SIDE;

    private static KeyBinding toggleCameraKey;
    private static KeyBinding nextModeKey;

    @Override
    public void onInitializeClient() {

        toggleCameraKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.cinecraft.toggle_camera",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category.cinecraft"
            )
        );

        nextModeKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.cinecraft.next_mode",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.cinecraft"
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (toggleCameraKey.wasPressed()) {
                cameraEnabled = !cameraEnabled;
            }

            while (nextModeKey.wasPressed()) {
                cycleMode();
            }
        });
    }

    private static void cycleMode() {
        CameraMode[] modes = CameraMode.values();

        currentMode = modes[
            (currentMode.ordinal() + 1) % modes.length
        ];
    }

    public static boolean isCameraEnabled() {
        return cameraEnabled;
    }

    public static CameraMode getCurrentMode() {
        return currentMode;
    }

    public static boolean isSideCameraEnabled() {
        return cameraEnabled && currentMode == CameraMode.SIDE;
    }
}
