package aurilux.armiger.common;

import aurilux.armiger.common.container.ContainerArmiger;
import aurilux.armiger.common.handler.CommonEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
    public static final int ARMIGER_GUI = 0;

    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case ARMIGER_GUI : return new ContainerArmiger(player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}