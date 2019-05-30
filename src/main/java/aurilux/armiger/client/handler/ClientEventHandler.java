package aurilux.armiger.client.handler;

import aurilux.armiger.client.ClientProxy;
import aurilux.armiger.common.Armiger;
import aurilux.armiger.common.network.PacketDispatcher;
import aurilux.armiger.common.network.messages.PacketOpenArmigerGui;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Armiger.MOD_ID)
public class ClientHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (FMLClientHandler.instance().getClient().inGameHasFocus && ClientProxy.OPEN_ARMIGER_GUI.isPressed()) {
            PacketDispatcher.INSTANCE.sendToServer(new PacketOpenArmigerGui());
        }
    }
}