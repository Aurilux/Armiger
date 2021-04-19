package aurilux.armiger.common.network;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.common.ArmigerMod;
import aurilux.armiger.common.network.messages.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.function.Function;

public class PacketHandler {
    private final static String protocol = "1";
    private static SimpleChannel CHANNEL;
    private static int index = 0;

    public static void init() {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ArmigerAPI.MOD_ID, "main"))
                .networkProtocolVersion(() -> protocol)
                .clientAcceptedVersions(protocol::equals)
                .serverAcceptedVersions(protocol::equals)
                .simpleChannel();

        // To client
        CHANNEL.registerMessage(index++, PacketSyncArmiger.class,
                PacketSyncArmiger::encode,
                PacketSyncArmiger::decode,
                PacketSyncArmiger::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        // To server
        CHANNEL.registerMessage(index++, PacketOpenArmigerGui.class,
                PacketOpenArmigerGui::encode,
                PacketOpenArmigerGui::decode,
                PacketOpenArmigerGui::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(index++, PacketSyncArmorChanges.class,
                PacketSyncArmorChanges::encode,
                PacketSyncArmorChanges::decode,
                PacketSyncArmorChanges::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(index++, PacketSyncSetsAndXp.class,
                PacketSyncSetsAndXp::encode,
                PacketSyncSetsAndXp::decode,
                PacketSyncSetsAndXp::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));

        // To both
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void sendTo(Object msg, ServerPlayerEntity player) {
        CHANNEL.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToAll(Object msg) {
        sendToAllExcept(msg, null);
    }

    public static void sendToAllExcept(Object msg, ServerPlayerEntity ignoredPlayer) {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            if (ignoredPlayer == null || player != ignoredPlayer) {
                CHANNEL.sendTo(msg, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
            else {
                ArmigerMod.LOG.debug("Ignored Player: " + ignoredPlayer.getDisplayName().getString());
            }
        }
    }
}