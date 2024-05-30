package com.cerbon.bosses_of_mass_destruction.entity.ai;

import com.cerbon.bosses_of_mass_destruction.entity.util.IEntity;
import net.minecraft.util.math.vector.Vector3d;

// https://gamedevelopment.tutsplus.com/tutorials/understanding-steering-behaviors-seek--gamedev-849
public class VelocitySteering implements ISteering {
    private final IEntity entity;
    private final double maxVelocity;
    private final double inverseMass;

    public VelocitySteering(IEntity entity, double maxVelocity, double mass) {
        if (mass == 0.0) throw new IllegalArgumentException("Mass cannot be zero");
        this.entity = entity;
        this.maxVelocity = maxVelocity;
        this.inverseMass = 1 / mass;
    }

    @Override
    public Vector3d accelerateTo(Vector3d target) {
        return target.subtract(entity.getPos())
                .normalize()
                .scale(maxVelocity)
                .subtract(entity.getDeltaMovement())
                .scale(inverseMass);
    }
}

