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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setPos(double x, double y, double z);

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Unique
    private static Vec3d cineCraft$smoothPos;

    @Unique
    private static Entity cineCraft$lastEntity;

    @Unique
    private static CineCraftClient.CameraMode cineCraft$lastMode;

    @Inject(method = "update", at = @At("TAIL"))
    private void cineCraft$updateCamera(
            BlockView area,
            Entity entity,
            boolean thirdPerson,
            boolean inverseView,
            float tickDelta,
            CallbackInfo ci
    ) {
        if (!thirdPerson || !CineCraftClient.isCameraEnabled()) {
            cineCraft$smoothPos = null;
            return;
        }

        CineCraftClient.CameraMode mode =
                CineCraftClient.getCurrentMode();

        double yaw = Math.toRadians(entity.getYaw(tickDelta));

        Vec3d center = entity.getCameraPosVec(tickDelta);
        Vec3d target;

        double back = Math.cos(yaw);
        double side = Math.sin(yaw);

        switch (mode) {
            case SIDE:
                target = center.add(side * 3.5, 0, -back * 3.5);
                break;

            case TOP:
                target = center.add(0, 6, 0);
                setRotation(entity.getYaw(tickDelta), 90);
                break;

            case LOW:
                target = center.add(-back * 3, 0.8, side * 3);
                break;

            case ORBIT:
                double orbit = System.currentTimeMillis() * 0.001;
                target = center.add(
                        Math.sin(orbit) * 4,
                        1.5,
                        Math.cos(orbit) * 4
                );
                break;

            case FRONT:
                target = center.add(back * 3, 0, side * 3);
                setRotation(entity.getYaw(tickDelta) + 180, 0);
                break;

            case WIDE:
                target = center.add(-back * 7, 2, -side * 7);
                break;

            case CLOSE_UP:
                target = center.add(-back * 1.5, 0.5, -side * 1.5);
                break;

            case TRACKING:
                target = center.add(-back * 4, 1.5, -side * 4);
                break;

            default:
                return;
        }

        if (cineCraft$smoothPos == null
                || cineCraft$lastEntity != entity
                || cineCraft$lastMode != mode) {
            cineCraft$smoothPos = ((Camera) (Object) this).getPos();
        }

        cineCraft$lastEntity = entity;
        cineCraft$lastMode = mode;

        cineCraft$smoothPos =
                cineCraft$smoothPos.lerp(target, 0.18);

        Vec3d start = center;

        BlockHitResult hit = area.raycast(new RaycastContext(
                start,
                cineCraft$smoothPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                entity
        ));

        Vec3d finalPos = cineCraft$smoothPos;

        if (hit.getType() != HitResult.Type.MISS) {
            double distance = start.distanceTo(hit.getPos());
            double total = start.distanceTo(finalPos);

            if (total > 0) {
                finalPos = start.lerp(
                        finalPos,
                        Math.max(0, distance - 0.2) / total
                );
            }
        }

        setPos(finalPos.x, finalPos.y, finalPos.z);
    }
}
