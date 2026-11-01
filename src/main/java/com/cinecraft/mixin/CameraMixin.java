package com.cinecraft.mixin;

import com.cinecraft.client.CineCraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
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
            BlockView area,
            Entity focusedEntity,
            boolean thirdPerson,
            boolean inverseView,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (!thirdPerson || !CineCraftClient.isSideCameraEnabled()) {
            return;
        }

        Vec3d current = ((Camera) (Object) this).getPos();

        double angle = Math.toRadians(focusedEntity.getYaw(tickDelta));
        double sideX = Math.cos(angle) * 3.5;
        double sideZ = Math.sin(angle) * 3.5;

        Vec3d start = focusedEntity.getCameraPosVec(tickDelta);
        Vec3d desired = current.add(sideX, 0, sideZ);

        BlockHitResult hit = area.raycast(new RaycastContext(
                start,
                desired,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                focusedEntity
        ));

        Vec3d finalPos = desired;

        if (hit.getType() != HitResult.Type.MISS) {
            double distance = start.distanceTo(hit.getPos());
            double total = start.distanceTo(desired);
            double safeDistance = Math.max(0, distance - 0.15);

            if (total > 0) {
                finalPos = start.lerp(desired, safeDistance / total);
            }
        }

        setPos(finalPos.x, finalPos.y, finalPos.z);
    }
}
