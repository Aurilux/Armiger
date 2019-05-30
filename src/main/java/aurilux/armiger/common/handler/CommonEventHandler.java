package aurilux.armiger.common.handler;

import aurilux.armiger.common.Armiger;
import aurilux.armiger.common.capability.ArmigerImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Armiger.MOD_ID)
public class CommonHandler {
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(ArmigerImpl.NAME, new ArmigerImpl.Provider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            //Get armiger from the old player...
            EntityPlayer player = event.getOriginal();
            ArmigerImpl.DefaultImpl inventory = ArmigerImpl.getCapability(player);
            NBTTagCompound data = inventory.serializeNBT();

            //..and give it to the new clone player
            player = event.getEntityPlayer();
            inventory = ArmigerImpl.getCapability(player);
            inventory.deserializeNBT(data);
        }
    }
}