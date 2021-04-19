package aurilux.armiger.common.network.messages;

import aurilux.armiger.api.ArmigerAPI;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public class PacketSyncArmorChanges {
    private final Map<Integer, ItemStack> changedArmor;
    private final Map<Integer, ItemStack> changedArmiger;

    public PacketSyncArmorChanges(Map<Integer, ItemStack> changedArmor, Map<Integer, ItemStack> changedArmiger) {
        this.changedArmor = changedArmor;
        this.changedArmiger = changedArmiger;
    }

    public static PacketSyncArmorChanges decode(PacketBuffer buf) {
        Map<Integer, ItemStack> changedArmor = new HashMap<>();
        Map<Integer, ItemStack> changedArmiger = new HashMap<>();

        while(buf.isReadable()) {
            changedArmor.put(buf.readInt(), buf.readItemStack());
            changedArmiger.put(buf.readInt(), buf.readItemStack());
        }
        return new PacketSyncArmorChanges(changedArmor, changedArmiger);
    }

    public static void encode(PacketSyncArmorChanges msg, PacketBuffer buf) {
        Iterator<Map.Entry<Integer, ItemStack>> armorIter = msg.changedArmor.entrySet().iterator();
        Iterator<Map.Entry<Integer, ItemStack>> armigerIter = msg.changedArmiger.entrySet().iterator();
        while (armorIter.hasNext() && armigerIter.hasNext()) {
            //armor
            Map.Entry<Integer, ItemStack> armorEntry = armorIter.next();
            buf.writeInt(armorEntry.getKey());
            buf.writeItemStack(armorEntry.getValue());

            //armiger
            Map.Entry<Integer, ItemStack> armigerEntry = armigerIter.next();
            buf.writeInt(armigerEntry.getKey());
            buf.writeItemStack(armigerEntry.getValue());
        }
    }

    public static void handle(PacketSyncArmorChanges msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(new Runnable() {
            // Have to use anon class instead of lambda or else we'll get classloading issues
            @Override
            public void run() {
                PlayerEntity player = ctx.get().getSender();
                ArmigerAPI.getCapability(player).ifPresent(armiger -> {
                    Iterator<Map.Entry<Integer, ItemStack>> armorIter = msg.changedArmor.entrySet().iterator();
                    Iterator<Map.Entry<Integer, ItemStack>> armigerIter = msg.changedArmiger.entrySet().iterator();
                    while (armorIter.hasNext() && armigerIter.hasNext()) {
                        //armor
                        Map.Entry<Integer, ItemStack> armorEntry = armorIter.next();
                        player.setItemStackToSlot(EquipmentSlotType.values()[5 - armorEntry.getKey()], armorEntry.getValue());

                        //armiger
                        Map.Entry<Integer, ItemStack> armigerEntry = armigerIter.next();
                        armiger.setStackInSlot(armigerEntry.getKey(), armigerEntry.getValue());
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}