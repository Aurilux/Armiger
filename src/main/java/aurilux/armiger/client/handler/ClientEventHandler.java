package aurilux.armiger.client.handler;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.client.Keybinds;
import aurilux.armiger.common.network.PacketHandler;
import aurilux.armiger.common.network.messages.PacketOpenArmigerGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArmigerAPI.MOD_ID)
public class ClientEventHandler {
    @SubscribeEvent
    public static void clientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (Keybinds.openArmiger.isPressed() && Minecraft.getInstance().isGameFocused()) {
            PacketHandler.sendToServer(new PacketOpenArmigerGui());
        }
    }
}