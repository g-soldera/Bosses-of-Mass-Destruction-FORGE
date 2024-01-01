package com.cerbon.bosses_of_mass_destruction.item.custom;

import com.cerbon.bosses_of_mass_destruction.particle.BMDParticles;
import com.cerbon.bosses_of_mass_destruction.util.Vec3Receiver;
import com.cerbon.cerbons_api.api.general.particle.ClientParticleBuilder;
import com.cerbon.cerbons_api.api.static_utilities.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;

public class BrimstoneNectarItemEffects implements Vec3Receiver {
    private final ClientParticleBuilder horizontalRodParticle = new ClientParticleBuilder(BMDParticles.GROUND_ROD.get())
            .color(f -> MathUtils.lerpVec(f, Vec3Colors.GOLD, Vec3Colors.RUNIC_BROWN))
            .colorVariation(0.25)
            .brightness(BMDParticles.FULL_BRIGHT)
            .age(10, 15);

    private final ClientParticleBuilder particleBuilder2 = new ClientParticleBuilder(BMDParticles.EARTHDIVE_INDICATOR.get())
            .color(f -> MathUtils.lerpVec(f, Vec3Colors.RED, Vec3Colors.DARK_RED))
            .colorVariation(0.25)
            .brightness(BMDParticles.FULL_BRIGHT)
            .age(30, 45);

    private final ClientParticleBuilder particleBuilder3 = new ClientParticleBuilder(BMDParticles.EARTHDIVE_INDICATOR.get())
            .color(f -> MathUtils.lerpVec(f, Vec3Colors.WHITE, Vec3Colors.GREY))
            .colorVariation(0.25)
            .brightness(BMDParticles.FULL_BRIGHT)
            .age(40, 50);

    @Override
    public void clientHandler(ClientLevel level, Vec3 vec3) {
        for (int i = 1; i <= 3; i++)
            spawnHorizontalRods(i, VecUtils.yOffset(vec3, 0.1));

        for (int i = 0; i <= 30; i++){
            Vec3 pos = VecUtils.yOffset(vec3, RandomUtils.range(0.0, 1.5));
            ParticleUtils.RotatingParticles particleParams = new ParticleUtils.RotatingParticles(pos, particleBuilder2, 1.5, 2.5, 0.0, 2.0);
            ParticleUtils.spawnRotatingParticles(particleParams);
        }

        for (int i = 0; i <= 30; i++){
            Vec3 pos = VecUtils.yOffset(vec3, RandomUtils.range(0.0, 1.5));
            ParticleUtils.RotatingParticles particleParams = new ParticleUtils.RotatingParticles(pos, particleBuilder3, 2.0, 3.0, -1.0, 0.0);
            ParticleUtils.spawnRotatingParticles(particleParams);
        }
    }

    private void spawnHorizontalRods(double radius, Vec3 pos){
        double numPoints = radius * 6;
        MathUtils.circleCallback(radius, (int) numPoints, VecUtils.yAxis, vec3 -> {
            Vec3 offset = vec3.add(new Vec3(RandomUtils.range(-0.5, 0.5), 0.0, RandomUtils.range(-0.5, 0.5)));
            horizontalRodParticle
                    .rotation((float) -MathUtils.directionToYaw(offset) + 90)
                    .build(pos.add(offset), Vec3.ZERO);
        });
    }
}
