package com.cinecraft.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class CineCraftSettingsScreen extends Screen {

    private final Screen parent;

    public CineCraftSettingsScreen(Screen parent) {
        super(Text.literal("CineCraft Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int startY = this.height / 2 - 65;

        addDrawableChild(new CameraSlider(
                centerX - 100, startY,
                "Camera Distance", 0.5, 2.0,
                CineCraftClient.getCameraDistance(), 0
        ));

        addDrawableChild(new CameraSlider(
                centerX - 100, startY + 35,
                "Camera Height", -3.0, 3.0,
                CineCraftClient.getCameraHeight(), 1
        ));

        addDrawableChild(new CameraSlider(
                centerX - 100, startY + 70,
                "Camera Smoothness", 0.05, 0.5,
                CineCraftClient.getCameraSmoothness(), 2
        ));

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Done"),
                button -> close()
        ).dimensions(centerX - 100, startY + 115, 200, 20).build());
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    private static class CameraSlider extends SliderWidget {

        private final String label;
        private final double min;
        private final double max;
        private final int setting;

        CameraSlider(
                int x, int y, String label,
                double min, double max,
                double current, int setting
        ) {
            super(
                    x, y, 200, 20,
                    Text.empty(),
                    (current - min) / (max - min)
            );

            this.label = label;
            this.min = min;
            this.max = max;
            this.setting = setting;

            updateMessage();
        }

        @Override
        protected void updateMessage() {
            double actual = min + value * (max - min);

            setMessage(Text.literal(
                    label + ": " + String.format("%.2f", actual)
            ));
        }

        @Override
        protected void applyValue() {
            double actual = min + value * (max - min);

            switch (setting) {
                case 0:
                    CineCraftClient.setCameraDistance(actual);
                    break;

                case 1:
                    CineCraftClient.setCameraHeight(actual);
                    break;

                case 2:
                    CineCraftClient.setCameraSmoothness((float) actual);
                    break;
            }
        }
    }
}
