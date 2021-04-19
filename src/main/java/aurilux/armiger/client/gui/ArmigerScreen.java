package aurilux.armiger.client.gui;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.api.IArmiger;
import aurilux.armiger.client.Keybinds;
import aurilux.armiger.common.container.ArmigerContainer;
import aurilux.armiger.common.core.ArmigerConfig;
import aurilux.armiger.common.network.PacketHandler;
import aurilux.armiger.common.network.messages.PacketSyncArmorChanges;
import aurilux.armiger.common.network.messages.PacketSyncSetsAndXp;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.gui.widget.button.LockIconButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class ArmigerScreen extends ContainerScreen<ArmigerContainer> {
    private final ResourceLocation ARMIGER_TEXTURE = new ResourceLocation(ArmigerAPI.MOD_ID, "textures/gui/armiger_gui.png");

    private final PlayerEntity player;
    private final IArmiger armiger;

    public ArmigerScreen(ArmigerContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.titleX = 97;
        this.player = inventory.player;
        // The presence of the IArmiger capability is checked before opening the gui
        this.armiger = ArmigerAPI.getCapability(player).resolve().get();
    }

    @Override
    public void init() {
        super.init();
        updateButtons();
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // Renders the background tint
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(@Nonnull MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1, 1, 1, 1);

        // Draw the armiger inventory texture. We draw this first so the vanilla inventory looks like its overlapping.
        getMinecraft().getTextureManager().bindTexture(ARMIGER_TEXTURE);
        blit(matrixStack, this.guiLeft - 72, this.guiTop, 0, 0, 72, 103, 128, 128);

        // Draw the vanilla inventory texture
        getMinecraft().getTextureManager().bindTexture(INVENTORY_BACKGROUND);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Draw the player entity in the inventory screen
        InventoryScreen.drawEntityOnScreen(this.guiLeft + 51, this.guiTop + 75, 30, (float)(this.guiLeft + 51) - mouseX,
                (float)(this.guiTop + 75 - 50) - mouseY, this.player);
    }

    private void switchArmorPieces(int index) {
        int startIndex = index * 4;
        HashMap<Integer, ItemStack> changedArmor = new HashMap<>();
        HashMap<Integer, ItemStack> changedArmiger = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            int armorSlotIndex = 5 + i; // 5 is the minimum armor slot index
            // Add 46 to account for all the default player inventory slots, including crafting slots
            int armigerSlotIndex = 46 + startIndex + i;
            Slot armorSlot = this.container.getSlot(armorSlotIndex);
            Slot armigerSlot = this.container.getSlot(armigerSlotIndex);

            if (armorSlot.canTakeStack(this.player)) {
                ItemStack temp = armigerSlot.getStack();
                armigerSlot.putStack(armorSlot.getStack());
                armorSlot.putStack(temp);

                changedArmor.put(i, armorSlot.getStack());
                changedArmiger.put(startIndex + i, armigerSlot.getStack());
            }
        }
        PacketHandler.sendToServer(new PacketSyncArmorChanges(changedArmor, changedArmiger));

        SoundEvent soundevent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
        player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), soundevent, SoundCategory.AMBIENT, 1.0f, 1.0f);
    }

    private void unlockSet(int xpCost, int index) {
        armiger.unlockSets(index + 1);
        player.addExperienceLevel(-xpCost);
        this.container.enableArmigerSlots();
        PacketHandler.sendToServer(new PacketSyncSetsAndXp(armiger.getUnlockedSets(), -xpCost));

        SoundEvent soundevent = SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
        player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), soundevent, SoundCategory.AMBIENT, 1.0f, .5f);

        updateButtons();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        //Close the screen if we press the keybinding again
        if (Keybinds.openArmiger.getKeyBinding().matchesKey(keyCode, scanCode)
                || getMinecraft().gameSettings.keyBindInventory.matchesKey(keyCode, scanCode)) {
            closeScreen();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void updateButtons() {
        buttons.clear();
        children.clear();
        for (int i = 0; i < 3; i++) {
            Button button;
            int finalI = i;
            if (i < armiger.getUnlockedSets()) {
                button = new ImageButton(this.guiLeft - (24 + (i * 21)), this.guiTop + 80, 20, 20, 84, 0, 21,
                        ARMIGER_TEXTURE, 128, 128, b -> switchArmorPieces(finalI));
            }
            else {
                int xpCost = (i - (armiger.getUnlockedSets() - 1)) * ArmigerConfig.COMMON.setUnlockCost.get();
                button = new UnlockSetButton(this.guiLeft - (24 + (i * 21)), this.guiTop + 80,
                        b -> unlockSet(xpCost, finalI),
                        (b, matrixStack, mouseX, mouseY) -> this.renderTooltip(matrixStack, new StringTextComponent("XP Cost: " + xpCost), mouseX, mouseY));
                button.active = player.experienceLevel >= xpCost;
            }
            addButton(button);
        }
    }

    // Just a small modification to show the unlocked icon on hover
    private static class UnlockSetButton extends LockIconButton {
        private final Button.ITooltip iTooltip;

        public UnlockSetButton(int x, int y, IPressable iPressable, ITooltip iTooltip) {
            super(x, y, iPressable);
            this.iTooltip = iTooltip;
            this.setLocked(true);
        }

        public void renderButton(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            Minecraft.getInstance().getTextureManager().bindTexture(Button.WIDGETS_LOCATION);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            LockIconButton.Icon icon;
            if (!this.active) {
                icon = LockIconButton.Icon.LOCKED_DISABLED;
            }
            else if (this.isHovered()) {
                icon = LockIconButton.Icon.UNLOCKED_HOVER;
            }
            else {
                icon = LockIconButton.Icon.LOCKED;
            }

            this.blit(matrixStack, this.x, this.y, icon.getX(), icon.getY(), this.width, this.height);
            if (this.isHovered()) {
                this.renderToolTip(matrixStack, mouseX, mouseY);
            }
        }

        // Copied from Button to get around its Button.ITooltip being final and no reasonable constructor to set it.
        @Override
        public void renderToolTip(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY) {
            this.iTooltip.onTooltip(this, matrixStack, mouseX, mouseY);
        }
    }
}