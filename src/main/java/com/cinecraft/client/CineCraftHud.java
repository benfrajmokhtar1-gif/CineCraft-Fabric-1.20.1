package com.cinecraft.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CineCraftHud {

    private static int displayTime = 0;
    private static boolean wasEnabled = false;
    private static CineCraftClient.CameraMode lastMode = null;

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            boolean enabled = CineCraftClient.isCameraEnabled();
            CineCraftClient.CameraMode mode =
                    CineCraftClient.getCurrentMode();

            if (enabled && (!wasEnabled || mode != lastMode)) {
                displayTime = 80;
            }

            if (!enabled) {
                displayTime = 0;
            } else if (displayTime > 0) {
                displayTime--;
            }

            wasEnabled = enabled;
            lastMode = mode;
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

            if (!CineCraftClient.isCameraEnabled()
                    || displayTime <= 0) {
                return;
            }

            MinecraftClient client = MinecraftClient.getInstance();

            int x = 10;
            int y = 10;

            drawContext.fill(
                    x, y, x + 132, y + 36,
                    0x990B0B0F
            );

            drawContext.fill(
                    x, y, x + 2, y + 36,
                    0xFFFF3030
            );

            drawContext.drawTextWithShadow(
                    client.textRenderer,
                    Text.literal("CINECRAFT"),
                    x + 9, y + 6,
                    0xFFFF5555
            );

            drawContext.drawTextWithShadow(
                    client.textRenderer,
                    Text.literal("MODE: "
                            + CineCraftClient.getCurrentMode().name()),
                    x + 9, y + 20,
                    0xFFFFFFFF
            );
        });
    }
}
