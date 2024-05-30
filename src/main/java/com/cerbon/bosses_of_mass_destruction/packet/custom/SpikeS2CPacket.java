package com.cerbon.bosses_of_mass_destruction.packet.custom;

import com.cerbon.bosses_of_mass_destruction.entity.custom.void_blossom.VoidBlossomEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpikeS2CPacket {
    private final int id;
    private final List<BlockPos> spikePositions;

    public SpikeS2CPacket(int id, List<BlockPos> spikePositions) {
        this.id = id;
        this.spikePositions = spikePositions;
    }

    public SpikeS2CPacket(PacketBuffer buf) {
        this.id = buf.readInt();
        int size = buf.readInt();
        this.spikePositions = new ArrayList<>();
        for (int i = 0; i < size; i++)
            this.spikePositions.add(buf.readBlockPos());
    }

    public void write(PacketBuffer buf){
        buf.writeInt(id);
        buf.writeInt(spikePositions.size());
        for (BlockPos spikePos : spikePositions){
            buf.writeBlockPos(spikePos);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            Minecraft client = Minecraft.getInstance();

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> client.execute(() -> {
                ClientWorld clientLevel = client.level;
                if (clientLevel == null) return;
                Entity entity = clientLevel.getEntity(id);

                if (entity instanceof VoidBlossomEntity){
                    spikePositions.forEach(spikePos -> ((VoidBlossomEntity) entity).clientSpikeHandler.addSpike(spikePos));
                }
            }));
        });
        ctx.setPacketHandled(true);
    }
}
