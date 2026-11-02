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
private static double cameraDistance = 1.0;
private static double cameraHeight = 0.0;
private static float cameraSmoothness = 0.18f;

public static double getCameraDistance() {
    return cameraDistance;
}

public static double getCameraHeight() {
    return cameraHeight;
}

public static float getCameraSmoothness() {
    return cameraSmoothness;
}

public static void setCameraDistance(double value) {
    cameraDistance = Math.max(0.5, Math.min(2.0, value));
}

public static void setCameraHeight(double value) {
    cameraHeight = Math.max(-3.0, Math.min(3.0, value));
}

public static void setCameraSmoothness(float value) {
    cameraSmoothness = Math.max(0.05f, Math.min(0.5f, value));
}
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
                CineCraftHud.register();
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
