package aurilux.armiger.common.capability;

import aurilux.armiger.api.ArmigerAPI;
import aurilux.armiger.api.IArmiger;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

public class ArmigerCapImpl extends ItemStackHandler implements IArmiger {
    public static final ResourceLocation NAME = new ResourceLocation(ArmigerAPI.MOD_ID, "armiger");
    private static final int NUM_SLOTS = 12;
    private final String UNLOCKED_SETS = "unlocked_sets";

    private int unlockedSets = 1;

    public ArmigerCapImpl() {
        super(NUM_SLOTS);
    }

    public int getUnlockedSets() {
        return unlockedSets;
    }

    public void unlockSets(int set) {
        if (set > 3) {
            unlockedSets = 3;
        }
        else {
            unlockedSets = Math.max(set, 1);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt(UNLOCKED_SETS, unlockedSets);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        unlockSets(nbt.getInt(UNLOCKED_SETS));
    }
}