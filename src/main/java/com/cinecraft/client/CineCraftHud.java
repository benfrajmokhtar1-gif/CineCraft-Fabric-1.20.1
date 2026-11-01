package com.cinecraft.client;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class CineCraftHud {

    public static void register() {

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {

            if (!CineCraftClient.isCameraEnabled()) {
                return;
            }

            MinecraftClient client = MinecraftClient.getInstance();

            int x = 12;
            int y = 12;

            drawContext.fill(x, y, x + 155, y + 65, 0xCC101010);

            drawContext.fill(x, y, x + 3, y + 65, 0xFFFF3030);

            drawContext.drawTextWithShadow(
                client.textRenderer,
                Text.literal("CINECRAFT"),
                x + 10, y + 7, 0xFFFF4040
            );

            drawContext.drawTextWithShadow(
                client.textRenderer,
                Text.literal("CAMERA: ON"),
                x + 10, y + 23, 0xFFFFFFFF
            );

            drawContext.drawTextWithShadow(
                client.textRenderer,
                Text.literal("MODE: " + CineCraftClient.getCurrentMode().name()),
                x + 10, y + 37, 0xFFFFD0D0
            );

            drawContext.drawTextWithShadow(
                client.textRenderer,
                Text.literal("V: Toggle  |  B: Next"),
                x + 10, y + 51, 0xFFAAAAAA
            );
        });
    }
}
