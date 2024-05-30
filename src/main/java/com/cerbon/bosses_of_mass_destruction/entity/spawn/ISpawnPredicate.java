package com.cerbon.bosses_of_mass_destruction.entity.spawn;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

public interface ISpawnPredicate {
    boolean canSpawn(Vector3d pos, Entity entity);
}
