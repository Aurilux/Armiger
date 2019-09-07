package aurilux.armiger.client;

import aurilux.armiger.client.gui.GuiArmiger;
import aurilux.armiger.common.CommonProxy;
import aurilux.armiger.common.capability.ArmigerImpl;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {
    public static final KeyBinding OPEN_ARMIGER_GUI = new KeyBinding("keybind.armiger.openArmigerGui", Keyboard.KEY_G, "keybind.armiger.category");

    @Override
    public void registerHandlers() {
        super.registerHandlers();
        ClientRegistry.registerKeyBinding(OPEN_ARMIGER_GUI);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ARMIGER_GUI :
                ArmigerImpl.DefaultImpl armiger = ArmigerImpl.getCapability(player);
                return new GuiArmiger(player, armiger);
        }
        return null;
    }
}