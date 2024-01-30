package com.cerbon.bosses_of_mass_destruction.entity.spawn;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface ISpawnPredicate {
    boolean canSpawn(Vec3 pos, Entity entity);
}
