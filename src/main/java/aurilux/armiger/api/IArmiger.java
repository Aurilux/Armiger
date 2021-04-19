package aurilux.armiger.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IArmiger extends INBTSerializable<CompoundNBT>, IItemHandlerModifiable {
    int getUnlockedSets();
    void unlockSets(int set);
}