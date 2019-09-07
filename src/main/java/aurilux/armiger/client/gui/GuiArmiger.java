package aurilux.armiger.client.gui;

import aurilux.armiger.client.ClientProxy;
import aurilux.armiger.common.Armiger;
import aurilux.armiger.common.capability.ArmigerImpl;
import aurilux.armiger.common.container.ContainerArmiger;
import aurilux.armiger.common.network.PacketDispatcher;
import aurilux.armiger.common.network.messages.PacketSyncArmorSlots;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.io.IOException;
import java.util.HashMap;

public class GuiArmiger extends GuiContainer {
    private final ResourceLocation bgTexture = new ResourceLocation(Armiger.MOD_ID, "textures/gui/armiger_gui.png");

    private EntityPlayer player;
    private ArmigerImpl.DefaultImpl armiger;
    private int unlockedSets;

    public GuiArmiger(EntityPlayer player, ArmigerImpl.DefaultImpl impl) {
        super(new ContainerArmiger(player, impl));
        this.armiger = impl;
        unlockedSets = armiger.getUnlockedSets() + 1;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.buttonList.clear();
        for (int i = 0; i < 3; i++) {
            GuiButton button;
            if (i < unlockedSets) {
                button = new GuiButtonImage(i, this.guiLeft - (24 + (i * 21)), this.guiTop + 80, 20, 18, 0, 167, 19, bgTexture);
            }
            else {
                button = new GuiButton(i, this.guiLeft - (24 + (i * 21)), this.guiTop + 40, 20, 18, "blah");
            }
            this.buttonList.add(button);
        }

        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        //Draw the background texture
        this.mc.getTextureManager().bindTexture(bgTexture);
        this.drawTexturedModalRect(this.guiLeft - 72, this.guiTop, 0, 0, this.xSize + 72, this.ySize);

        //Draw the player entity in the inventory screen
        GuiInventory.drawEntityOnScreen(this.guiLeft + 51, this.guiTop + 75, 30, (float)(this.guiLeft + 51) - mouseX,
                (float)(this.guiTop + 75 - 50) - mouseY, this.mc.player);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        int minIndex = button.id * 4;
        HashMap<Integer, ItemStack> changedArmor = new HashMap<>();
        HashMap<Integer, ItemStack> changedArmiger = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            int armorSlotIndex = 5 + i; //5 is the minimum armor slot index
            int armigerSlotIndex = 46 + minIndex + i; //add 46 to account for all the default player inventory slots
            Slot armorSlot = this.inventorySlots.getSlot(armorSlotIndex);
            Slot armigerSlot = this.inventorySlots.getSlot(armigerSlotIndex);

            if (armorSlot.canTakeStack(this.mc.player)) {
                ItemStack temp = armigerSlot.getStack();
                armigerSlot.putStack(armorSlot.getStack());
                armorSlot.putStack(temp);

                changedArmor.put(i, armorSlot.getStack());
                changedArmiger.put(minIndex + i, armigerSlot.getStack());
            }
        }

        SoundEvent soundevent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
        player.world.playSound(player, player.posX, player.posY, player.posZ, soundevent, SoundCategory.AMBIENT, 1.0f, 1.0f);
        PacketDispatcher.INSTANCE.sendToServer(new PacketSyncArmorSlots(changedArmor, changedArmiger));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        //Close the screen if we press the keybinding again
        if (ClientProxy.OPEN_ARMIGER_GUI.getKeyCode() == keyCode && this.mc.currentScreen instanceof GuiArmiger) {
            this.mc.player.closeScreen();
        }
    }
}