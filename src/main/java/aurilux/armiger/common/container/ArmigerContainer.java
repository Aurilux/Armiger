package aurilux.armiger.common.container;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.api.IArmiger;
import aurilux.armiger.common.core.ModObjects;
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ArmigerContainer extends PlayerContainer {
    private static final ResourceLocation[] ARMOR_SLOT_TEXTURES = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS,
            EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlotType[] VALID_EQUIPMENT_SLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD,
            EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

    private final IArmiger armiger;
    private final List<ArmigerSlot> armigerSlots = new ArrayList<>();

    public ArmigerContainer(int windowId, PlayerInventory inventory) {
        super(inventory, inventory.player.world.isRemote, inventory.player);
        // The presence of the IArmiger capability is checked before opening the gui
        this.armiger = ArmigerAPI.getCapability(player).resolve().get();
        this.windowId = windowId;
        this.containerType = ModObjects.ARMIGER_CONTAINER.get();

        // We generate slots for all three sets, then enable them depending on how many the player has unlocked
        int index = 0;
        for (int i = 0; i < 3; i++) { //three columns
            for (int j = 0; j < 4; j++) { //four slots
                EquipmentSlotType equipmentSlotType = VALID_EQUIPMENT_SLOTS[j];
                ArmigerSlot armigerSlot = new ArmigerSlot(armiger, index, -(22 + (i * 21)), 8 + (j * 18), equipmentSlotType);
                armigerSlots.add(armigerSlot);
                this.addSlot(armigerSlot);
                index++;
            }
        }
        enableArmigerSlots();
    }

    public void enableArmigerSlots() {
        int maxEnabledIndex = armiger.getUnlockedSets() * 4;
        for (int i = 0; i < armigerSlots.size(); i++) {
            armigerSlots.get(i).setEnabled(i < maxEnabledIndex);
        }
    }

    //Minecraft instantiates player armor slots through an anonymous class. I feel it's better organized to do it like this.
    private class ArmigerSlot extends SlotItemHandler {
        private final EquipmentSlotType equipmentSlotType;
        private boolean enabled = false;

        public ArmigerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, EquipmentSlotType equipmentSlot){
            super(itemHandler, index, xPosition, yPosition);
            equipmentSlotType = equipmentSlot;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.canEquip(equipmentSlotType, ArmigerContainer.this.player);
        }

        @Override
        public boolean canTakeStack(PlayerEntity playerIn) {
            ItemStack itemstack = this.getStack();
            return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) &&
                    super.canTakeStack(playerIn);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public Pair<ResourceLocation, ResourceLocation> getBackground() {
            return Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE,
                    ARMOR_SLOT_TEXTURES[equipmentSlotType.getIndex()]);
        }
    }
}