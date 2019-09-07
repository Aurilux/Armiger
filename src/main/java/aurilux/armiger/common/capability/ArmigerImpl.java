package aurilux.armiger.common.capability;

import aurilux.armiger.common.Armiger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArmigerImpl {
    @CapabilityInject(IArmiger.class)
    public static final Capability<IArmiger> ARMIGER_CAPABILITY = null;

    public static final ResourceLocation NAME = new ResourceLocation(Armiger.MOD_ID, "armiger");

    public static void register() {
        CapabilityManager.INSTANCE.register(IArmiger.class, new Storage(), DefaultImpl::new);
    }

    public static DefaultImpl getCapability(EntityPlayer player) {
        return (DefaultImpl) player.getCapability(ARMIGER_CAPABILITY, null);
    }

    public interface IArmiger {
        int getUnlockedSets();
        void unlockSlots(int set);
    }

    private static class Storage implements Capability.IStorage<IArmiger> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IArmiger> capability, IArmiger instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IArmiger> capability, IArmiger instance, EnumFacing side, NBTBase nbt) {
            //do nothing
        }
    }

    public static class DefaultImpl extends ItemStackHandler implements IArmiger {
        private static final int ARMIGER_SLOTS = 12;
        private int unlockedSlots = 0;

        public DefaultImpl() {
            super(ARMIGER_SLOTS);
        }

        public int getUnlockedSets() {
            return unlockedSlots;
        }

        public void unlockSlots(int set) {
            if (set > 2 && set < 0) {
                return;
            }

            unlockedSlots = set;
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private DefaultImpl instance = new DefaultImpl();

        public Provider() {}

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == ARMIGER_CAPABILITY;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return hasCapability(capability, facing) ? (T) this.instance : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return this.instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            this.instance.deserializeNBT(nbt);
        }
    }
}
