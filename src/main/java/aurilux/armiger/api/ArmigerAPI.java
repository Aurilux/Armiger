package aurilux.armiger.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

public class ArmigerAPI {
    public static final String MOD_ID = "armiger";

    @CapabilityInject(IArmiger.class)
    public static final Capability<IArmiger> ARMIGER_CAPABILITY = null;

    public static LazyOptional<IArmiger> getCapability(PlayerEntity player) {
        return player.getCapability(ARMIGER_CAPABILITY);
    }
}