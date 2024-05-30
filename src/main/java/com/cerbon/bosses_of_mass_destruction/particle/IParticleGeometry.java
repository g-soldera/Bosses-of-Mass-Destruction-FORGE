package com.cerbon.bosses_of_mass_destruction.particle;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.ActiveRenderInfo;

@FunctionalInterface
public interface IParticleGeometry {
    Vector3f[] getGeometry(
            ActiveRenderInfo camera,
            float tickDelta,
            double prevPosX,
            double prevPosY,
            double prevPosZ,
            double x,
            double y,
            double z,
            float scale,
            float rotation
    );
}

