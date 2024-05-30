package com.cerbon.bosses_of_mass_destruction.entity.custom.void_blossom;

import com.cerbon.bosses_of_mass_destruction.api.maelstrom.general.event.EventScheduler;
import com.cerbon.bosses_of_mass_destruction.api.maelstrom.general.event.EventSeries;
import com.cerbon.bosses_of_mass_destruction.api.maelstrom.general.event.TimedEvent;
import com.cerbon.bosses_of_mass_destruction.api.maelstrom.static_utilities.VecUtils;
import com.cerbon.bosses_of_mass_destruction.block.BMDBlocks;
import com.cerbon.bosses_of_mass_destruction.entity.ai.action.IActionWithCooldown;
import com.cerbon.bosses_of_mass_destruction.entity.custom.void_blossom.hitbox.HitboxId;
import com.cerbon.bosses_of_mass_destruction.entity.custom.void_blossom.hitbox.NetworkedHitboxManager;
import com.cerbon.bosses_of_mass_destruction.packet.BMDPacketHandler;
import com.cerbon.bosses_of_mass_destruction.packet.custom.PlaceS2CPacket;
import com.cerbon.bosses_of_mass_destruction.sound.BMDSounds;
import com.cerbon.bosses_of_mass_destruction.util.BMDUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlossomAction implements IActionWithCooldown {
    private final VoidBlossomEntity entity;
    private final EventScheduler eventScheduler;
    private final Supplier<Boolean> shouldCancel;

    private final List<Vector3d> blossomPositions = Stream.of(
            VecUtils.xAxis,
            VecUtils.zAxis,
            VecUtils.xAxis.reverse(),
            VecUtils.zAxis.reverse(),
            VecUtils.xAxis.add(VecUtils.zAxis),
            VecUtils.xAxis.add(VecUtils.zAxis.reverse()),
            VecUtils.xAxis.reverse().add(VecUtils.zAxis),
            VecUtils.xAxis.reverse().add(VecUtils.zAxis.reverse())
    ).map(vec3 -> vec3.normalize().scale(15.0)).collect(Collectors.toList());

    public BlossomAction(VoidBlossomEntity entity, EventScheduler eventScheduler, Supplier<Boolean> shouldCancel) {
        this.entity = entity;
        this.eventScheduler = eventScheduler;
        this.shouldCancel = shouldCancel;
    }

    @Override
    public int perform() {
        World level = entity.level;
        if (!(level instanceof ServerWorld)) return 80;

        eventScheduler.addEvent(
                new EventSeries(
                        new TimedEvent(
                                () -> entity.getEntityData().set(NetworkedHitboxManager.hitbox, HitboxId.SpikeWave3.getId()),
                                20,
                                1,
                                shouldCancel
                        ),
                        new TimedEvent(
                                () -> entity.getEntityData().set(NetworkedHitboxManager.hitbox, HitboxId.Idle.getId()),
                                80
                        )
                )
        );
        placeBlossoms((ServerWorld) level);
        return 120;
    }

    private void placeBlossoms(ServerWorld level){
        List<BlockPos> positions = blossomPositions.stream()
                .map(pos -> new BlockPos(pos.add(entity.position())))
                .collect(Collectors.toList());
        Collections.shuffle(positions);

        float hpRatio = entity.getHealth() / entity.getMaxHealth();
        int protectedPositions;

        if (hpRatio < VoidBlossomEntity.hpMilestones.get(1))
            protectedPositions = 6;
        else if (hpRatio < VoidBlossomEntity.hpMilestones.get(2))
            protectedPositions = 3;
        else
            protectedPositions = 0;

        BMDUtils.playSound(level, entity.position(), BMDSounds.SPIKE_WAVE_INDICATOR.get(), SoundCategory.HOSTILE, 2.0f, 0.7f, 64.0, null);

        for (int i = 0; i < 8; i++){
            int i1 = i;
            eventScheduler.addEvent(
                    new TimedEvent(
                            () -> {
                                BlockPos blossomPos = positions.get(i1);
                                level.setBlockAndUpdate(blossomPos, Blocks.GRASS_BLOCK.defaultBlockState());
                                level.setBlockAndUpdate(blossomPos.above(), BMDBlocks.VOID_BLOSSOM.get().defaultBlockState());
                                BMDPacketHandler.sendToAllPlayersTrackingChunk(new PlaceS2CPacket(VecUtils.asVec3(blossomPos).add(VecUtils.unit.scale(0.5))), level, entity.position());
                                BMDUtils.playSound(level, VecUtils.asVec3(blossomPos), BMDSounds.PETAL_BLADE.get(), SoundCategory.HOSTILE, 1.0f, BMDUtils.randomPitch(entity.getRandom()), 64, null);

                                if(i1 < protectedPositions) {
                                    for (int x = -1; x <= 1; x++) {
                                        for (int z = -1; z <= 1; z++) {
                                            for (int y = 0; y <= 2; y++) {
                                                if ((x != 0 || z != 0)) {
                                                    level.setBlockAndUpdate(blossomPos.offset(x, y, z), BMDBlocks.VINE_WALL.get().defaultBlockState());
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            40 + i * 8,
                            1,
                            shouldCancel
                    )
            );
        }

    }
}
