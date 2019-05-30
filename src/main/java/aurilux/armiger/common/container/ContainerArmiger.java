package aurilux.armiger.common.container;


import aurilux.armiger.common.capability.ArmigerImpl;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerArmiger extends ContainerPlayer {
    private final EntityEquipmentSlot[] equipmentSlots = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD,
            EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    private EntityPlayer player;
    private ArmigerImpl.DefaultImpl armiger;

    public ContainerArmiger(EntityPlayer player) {
        super(player.inventory, player.world.isRemote, player);

        this.player = player;
        armiger = ArmigerImpl.getCapability(player);

        //armiger armor slots
        int index = 0;
        for (int i = 0; i < 3; i++) { //three columns
            for (int j = 0; j < 4; j++) { //four slots
                EntityEquipmentSlot slot = equipmentSlots[j];
                this.addSlotToContainer(new ArmorSlot(armiger, index, -(22 + (i * 21)), 8 + (j * 18), slot));
                index++;
            }
        }
    }

    //Minecraft instantiates player armor slots through an anonymous class. I feel it's better organized to do it like this.
    private class ArmorSlot extends SlotItemHandler {
        private EntityEquipmentSlot entityEquipmentSlot;

        public ArmorSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, EntityEquipmentSlot equipmentSlot){
            super(itemHandler, index, xPosition, yPosition);
            entityEquipmentSlot = equipmentSlot;
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem().isValidArmor(stack, entityEquipmentSlot, player);
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
            ItemStack itemstack = this.getStack();
            return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) &&
                    super.canTakeStack(playerIn);
        }

        @Override
        public String getSlotTexture() {
            return ItemArmor.EMPTY_SLOT_NAMES[entityEquipmentSlot.getIndex()];
        }
    }
}