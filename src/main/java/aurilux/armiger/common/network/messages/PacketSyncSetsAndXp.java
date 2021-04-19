package aurilux.armiger.common.network.messages;

import aurilux.armiger.api.ArmigerAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncSetsAndXp {
    private final int unlockedSets;
    private final int xpChange;

    public PacketSyncSetsAndXp(int unlockedSets, int xpChange) {
        this.unlockedSets = unlockedSets;
        this.xpChange = xpChange;
    }

    public static PacketSyncSetsAndXp decode(PacketBuffer buf) {
        return new PacketSyncSetsAndXp(buf.readInt(), buf.readInt());
    }

    public static void encode(PacketSyncSetsAndXp msg, PacketBuffer buf) {
        buf.writeInt(msg.unlockedSets);
        buf.writeInt(msg.xpChange);
    }

    public static void handle(PacketSyncSetsAndXp msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                PlayerEntity player = ctx.get().getSender();
                if (player != null) {
                    ArmigerAPI.getCapability(player).ifPresent(armiger -> {
                        armiger.unlockSets(msg.unlockedSets);
                        player.addExperienceLevel(msg.xpChange);
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}