package aurilux.armiger.common.network.messages;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.common.container.ArmigerContainerProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class PacketOpenArmigerGui {
    public static PacketOpenArmigerGui decode(PacketBuffer buf) {
        return new PacketOpenArmigerGui();
    }

    public static void encode(PacketOpenArmigerGui msg, PacketBuffer buf) {
    }

    public static void handle(PacketOpenArmigerGui msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                ServerPlayerEntity player = ctx.get().getSender();
                if (player != null) {
                    ArmigerAPI.getCapability(player).ifPresent(c ->
                            NetworkHooks.openGui(player, new ArmigerContainerProvider())
                    );
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}