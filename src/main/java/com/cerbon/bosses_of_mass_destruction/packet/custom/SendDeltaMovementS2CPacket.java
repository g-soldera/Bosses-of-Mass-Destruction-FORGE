package com.cerbon.bosses_of_mass_destruction.packet.custom;

import com.cerbon.bosses_of_mass_destruction.api.maelstrom.static_utilities.PacketUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SendDeltaMovementS2CPacket {
    private final Vec3 deltaMovement;

    public SendDeltaMovementS2CPacket(Vec3 deltaMovement){
        this.deltaMovement = deltaMovement;
    }

    public SendDeltaMovementS2CPacket(FriendlyByteBuf buf){
        this.deltaMovement = PacketUtils.readVec3(buf);
    }

    public void write(FriendlyByteBuf buf){
        PacketUtils.writeVec3(buf, this.deltaMovement);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            LocalPlayer localPlayer = Minecraft.getInstance().player;
            if (localPlayer == null) return;

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().execute(() -> localPlayer.setDeltaMovement(this.deltaMovement)));
        });
        ctx.setPacketHandled(true);
    }
}
