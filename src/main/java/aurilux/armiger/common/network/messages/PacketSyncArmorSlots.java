package aurilux.armiger.common.network.messages;

import aurilux.armiger.common.capability.ArmigerImpl;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PacketSyncArmorSlots extends AbstractPacket<PacketSyncArmorSlots> {
    private HashMap<Integer, ItemStack> changedArmor = new HashMap<>();
    private HashMap<Integer, ItemStack> changedArmiger = new HashMap<>();
    private int amount;

    public PacketSyncArmorSlots() {}

    public PacketSyncArmorSlots(HashMap changedArmor, HashMap changedArmiger) {
        this.changedArmor = changedArmor;
        this.changedArmiger = changedArmiger;
        amount = changedArmor.size();
    }

    @Override
    public void handleClientSide(PacketSyncArmorSlots message, EntityPlayer player) {

    }

    @Override
    public void handleServerSide(PacketSyncArmorSlots message, EntityPlayer player) {
        ArmigerImpl.DefaultImpl armiger = ArmigerImpl.getCapability(player);

        Iterator<Map.Entry<Integer, ItemStack>> armorIter = message.changedArmor.entrySet().iterator();
        Iterator<Map.Entry<Integer, ItemStack>> armigerIter = message.changedArmiger.entrySet().iterator();
        while (armorIter.hasNext() && armigerIter.hasNext()) {
            //armor
            Map.Entry<Integer, ItemStack> armorEntry = armorIter.next();
            player.setItemStackToSlot(EntityEquipmentSlot.values()[5 - armorEntry.getKey()], armorEntry.getValue());

            //armiger
            Map.Entry<Integer, ItemStack> armigerEntry = armigerIter.next();
            armiger.setStackInSlot(armigerEntry.getKey(), armigerEntry.getValue());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        amount = buf.readInt();
        for (int i = 0; i < amount; i++) {
            //armor
            changedArmor.put(buf.readInt(), ByteBufUtils.readItemStack(buf));
            //armiger
            changedArmiger.put(buf.readInt(), ByteBufUtils.readItemStack(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(amount);

        Iterator<Map.Entry<Integer, ItemStack>> armorIter = changedArmor.entrySet().iterator();
        Iterator<Map.Entry<Integer, ItemStack>> armigerIter = changedArmiger.entrySet().iterator();
        while (armorIter.hasNext() && armigerIter.hasNext()) {
            //armor
            Map.Entry<Integer, ItemStack> armorEntry = armorIter.next();
            buf.writeInt(armorEntry.getKey());
            ByteBufUtils.writeItemStack(buf, armorEntry.getValue());

            //armiger
            Map.Entry<Integer, ItemStack> armigerEntry = armigerIter.next();
            buf.writeInt(armigerEntry.getKey());
            ByteBufUtils.writeItemStack(buf, armigerEntry.getValue());
        }
    }
}