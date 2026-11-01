package com.cinecraft.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CineCraftHud {

    private static int displayTime = 0;
    private static int guideTime = 0;

    private static boolean wasEnabled = false;
    private static boolean wasThirdPerson = false;
    private static boolean hasShownGuide = false;

    private static CineCraftClient.CameraMode lastMode = null;

    public static void register() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            boolean enabled = CineCraftClient.isCameraEnabled();
            boolean thirdPerson =
                    !client.options.getPerspective().isFirstPerson();

            CineCraftClient.CameraMode mode =
                    CineCraftClient.getCurrentMode();

            // First-time player guide
            if (thirdPerson && !wasThirdPerson && !hasShownGuide) {
                guideTime = 160;
                hasShownGuide = true;
            }

            if (guideTime > 0) {
                guideTime--;
            }

            // Main HUD timer
            if (enabled && (!wasEnabled || mode != lastMode)) {
                displayTime = 80;
            }

            if (!enabled) {
                displayTime = 0;
            } else if (displayTime > 0) {
                displayTime--;
            }

            wasEnabled = enabled;
            wasThirdPerson = thirdPerson;
            lastMode = mode;
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

            MinecraftClient client = MinecraftClient.getInstance();

            // Main cinematic HUD
            if (CineCraftClient.isCameraEnabled() && displayTime > 0) {

                int x = 10;
                int y = 10;

                drawContext.fill(
                        x, y, x + 155, y + 49,
                        0x990B0B0F
                );

                drawContext.fill(
                        x, y, x + 2, y + 49,
                        0xFFFF3030
                );

                drawContext.drawTextWithShadow(
                        client.textRenderer,
                        Text.literal("CINECRAFT"),
                        x + 9, y + 5,
                        0xFFFF5555
                );

                drawContext.drawTextWithShadow(
                        client.textRenderer,
                        Text.literal("MODE: "
                                + CineCraftClient.getCurrentMode().name()),
                        x + 9, y + 17,
                        0xFFFFFFFF
                );

                drawContext.drawTextWithShadow(
                        client.textRenderer,
                        Text.literal("V: Toggle Camera"),
                        x + 9, y + 29,
                        0xFFFFD0D0
                );

                drawContext.drawTextWithShadow(
                        client.textRenderer,
                        Text.literal("B: Next Mode"),
                        x + 9, y + 39,
                        0xFFAAAAAA
                );
            }

            // New player guide
            if (guideTime > 0) {

                String line1 = "CINECRAFT: Press V to activate";
                String line2 = "Press B to change camera modes";

                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();

                int panelWidth = 210;
                int panelHeight = 38;

                int x = (width - panelWidth) / 2;
                int y = height - 65;

                drawContext.fill(
                        x, y, x + panelWidth, y + panelHeight,
                        0xCC0B0B0F
                );

                drawContext.fill(
                        x, y, x + 2, y + panelHeight,
                        0xFFFF3030
                );

                drawContext.drawTextWithShadow(
                        client.textRenderer,
                        Text.literal(line1),
                        x + 10, y + 7,
                        0xFFFFFFFF
                );

                drawContext.drawTextWithShadow(
                        client.textRenderer,
                        Text.literal(line2),
                        x + 10, y + 21,
                        0xFFFFAAAA
                );
            }
        });
    }
}
