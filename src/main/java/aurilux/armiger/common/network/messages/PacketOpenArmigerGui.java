package aurilux.armiger.common.network.messages;

import aurilux.armiger.common.Armiger;
import aurilux.armiger.common.CommonProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketOpenArmigerGui extends AbstractPacket<PacketOpenArmigerGui> {
    @Override
    public void handleClientSide(PacketOpenArmigerGui message, EntityPlayer player) {

    }

    @Override
    public void handleServerSide(PacketOpenArmigerGui message, EntityPlayer player) {
        player.openContainer.onContainerClosed(player);
        player.openGui(Armiger.MOD_ID, CommonProxy.ARMIGER_GUI, player.world, 0, 0, 0);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }
}