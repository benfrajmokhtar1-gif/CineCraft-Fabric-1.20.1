package com.cinecraft.mixin;

import com.cinecraft.client.CineCraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setPos(double x, double y, double z);

    @Inject(method = "update", at = @At("TAIL"))
    private void cineCraft$sideCamera(
            net.minecraft.world.BlockView area,
            Entity focusedEntity,
            boolean thirdPerson,
            boolean inverseView,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (!thirdPerson || !CineCraftClient.isSideCameraEnabled()) {
            return;
        }

        Vec3d pos = ((Camera) (Object) this).getPos();
        float yaw = focusedEntity.getYaw(tickDelta);
        double angle = Math.toRadians(yaw);

        double sideX = Math.cos(angle) * 3.5;
        double sideZ = Math.sin(angle) * 3.5;

        setPos(pos.x + sideX, pos.y, pos.z + sideZ);
    }
}
