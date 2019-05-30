package aurilux.armiger.common;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerArmiger extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }
}