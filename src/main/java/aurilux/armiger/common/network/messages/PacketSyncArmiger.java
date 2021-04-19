package aurilux.armiger.common.network.messages;

import aurilux.armiger.api.ArmigerAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncArmiger {
    private final CompoundNBT nbt;

    public PacketSyncArmiger(CompoundNBT nbt) {
        this.nbt = nbt;
    }

    public static PacketSyncArmiger decode(PacketBuffer buf) {
        return new PacketSyncArmiger(buf.readCompoundTag());
    }

    public static void encode(PacketSyncArmiger msg, PacketBuffer buf) {
        buf.writeCompoundTag(msg.nbt);
    }

    public static void handle(PacketSyncArmiger msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                PlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    ArmigerAPI.getCapability(player).ifPresent(armiger -> armiger.deserializeNBT(msg.nbt));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}