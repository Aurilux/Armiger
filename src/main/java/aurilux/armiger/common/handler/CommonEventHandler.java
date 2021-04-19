package aurilux.armiger.common.handler;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.common.capability.ArmigerCapImpl;
import aurilux.armiger.common.core.ArmigerConfig;
import aurilux.armiger.common.network.PacketHandler;
import aurilux.armiger.common.network.messages.PacketSyncArmiger;
import aurilux.armiger.common.util.CapabilityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArmigerAPI.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            CapabilityHelper.attach(event, ArmigerCapImpl.NAME, ArmigerAPI.ARMIGER_CAPABILITY, new ArmigerCapImpl());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ArmigerAPI.getCapability(player).ifPresent(a -> {
            if (ArmigerConfig.COMMON.setUnlockCost.get() == 0) {
                a.unlockSets(3);
            }
            PacketHandler.sendTo(new PacketSyncArmiger(a.serializeNBT()), player);
        });
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        ArmigerAPI.getCapability(event.getOriginal()).ifPresent(oldCap ->
                ArmigerAPI.getCapability(event.getPlayer()).ifPresent(newCap ->
                        newCap.deserializeNBT(oldCap.serializeNBT())));
    }

    @SubscribeEvent
    public static void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ArmigerAPI.getCapability(player).ifPresent(a ->
                PacketHandler.sendTo(new PacketSyncArmiger(a.serializeNBT()), player));
    }

    @SubscribeEvent
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        ArmigerAPI.getCapability(player).ifPresent(a ->
                PacketHandler.sendTo(new PacketSyncArmiger(a.serializeNBT()), player));
    }
}